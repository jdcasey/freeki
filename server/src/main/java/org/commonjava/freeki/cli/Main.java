/*******************************************************************************
 * Copyright (C) 2013 John Casey.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.commonjava.freeki.cli;

import groovy.text.GStringTemplateEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import org.commonjava.freeki.conf.FreekiConfig;
import org.commonjava.freeki.conf.GTemplateConfig;
import org.commonjava.freeki.data.FreekiStore;
import org.commonjava.freeki.data.GitManager;
import org.commonjava.freeki.data.TemplateController;
import org.commonjava.freeki.infra.auth.Authorizer;
import org.commonjava.freeki.infra.render.ContentRenderer;
import org.commonjava.freeki.infra.render.RenderingEngine;
import org.commonjava.freeki.infra.render.json.JsonRenderer;
import org.commonjava.freeki.infra.render.tmpl.GTHtmlRenderer;
import org.commonjava.freeki.infra.render.tmpl.GTTextRenderer;
import org.commonjava.freeki.rest.GroupContentHandler;
import org.commonjava.freeki.rest.OfflineContentHandler;
import org.commonjava.freeki.rest.PageContentHandler;
import org.commonjava.freeki.rest.StaticContentHandler;
import org.commonjava.freeki.rest.TemplateContentHandler;
import org.commonjava.freeki.rest.UpdateHandler;
import org.commonjava.freeki.util.ContentType;
import org.commonjava.vertx.vabr.ApplicationRouter;
import org.commonjava.vertx.vabr.RouteCollection;
import org.commonjava.vertx.vabr.RouteHandler;
import org.commonjava.web.json.ser.JsonSerializer;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.pegdown.PegDownProcessor;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.impl.DefaultVertx;
import org.vertx.java.platform.Verticle;

public class Main
    extends Verticle
{

    public static void main( final String[] args )
        throws IOException
    {
        new Main( args ).run();
    }

    private final PegDownProcessor proc;

    private final GStringTemplateEngine templates;

    private boolean canStart = false;

    private final FreekiConfig config;

    public Main( final String[] args )
    {
        config = new FreekiConfig();
        final CmdLineParser parser = new CmdLineParser( config );
        try
        {
            parser.parseArgument( args );
            canStart = true;
        }
        catch ( final CmdLineException e )
        {
            System.out.printf( "ERROR: %s", e.getMessage() );
            printUsage( parser, e );
        }

        if ( config.isHelp() )
        {
            printUsage( parser, null );
            canStart = false;
        }

        if ( canStart )
        {
            proc = new PegDownProcessor();
            templates = new GStringTemplateEngine();
        }
        else
        {
            proc = null;
            templates = null;
        }
    }

    private static void printUsage( final CmdLineParser parser, final Exception error )
    {
        if ( error != null )
        {
            System.err.println( "Invalid option(s): " + error.getMessage() );
            System.err.println();
        }

        System.err.println( "Usage: $0 [OPTIONS] [<target-path>]" );
        System.err.println();
        System.err.println();
        // If we are running under a Linux shell COLUMNS might be available for the width
        // of the terminal.
        parser.setUsageWidth( ( System.getenv( "COLUMNS" ) == null ? 100 : Integer.valueOf( System.getenv( "COLUMNS" ) ) ) );
        parser.printUsage( System.err );
        System.err.println();
    }

    public void run()
        throws IOException
    {
        if ( !canStart )
        {
            return;
        }

        start();
        final Vertx v = new DefaultVertx();
        setVertx( v );

        final GitManager git = new GitManager( config );
        final FreekiStore store = new FreekiStore( config, git );

        final Map<String, String> rawTemplateConf = new HashMap<>();
        rawTemplateConf.put( "group@" + ContentType.TEXT_HTML.value(), "groovy/html/group.groovy" );
        rawTemplateConf.put( "page@" + ContentType.TEXT_HTML.value(), "groovy/html/page.groovy" );
        rawTemplateConf.put( "group@" + ContentType.STATIC_HTML.value(), "groovy/static-html/group.groovy" );
        rawTemplateConf.put( "page@" + ContentType.STATIC_HTML.value(), "groovy/static-html/page.groovy" );
        rawTemplateConf.put( "group@" + ContentType.TEXT_PLAIN.value(), "groovy/plain/group.groovy" );
        rawTemplateConf.put( "page@" + ContentType.TEXT_PLAIN.value(), "groovy/plain/page.groovy" );

        final GTemplateConfig templateConfig = new GTemplateConfig( rawTemplateConf, config );

        final Set<ContentRenderer> renderers = new HashSet<>();
        renderers.add( new GTHtmlRenderer( templates, proc, templateConfig ) );
        renderers.add( new GTTextRenderer( templates, templateConfig ) );

        final JsonSerializer serializer = new JsonSerializer(/* new PrettyPrintingAdapter() */);
        renderers.add( new JsonRenderer( serializer ) );

        final RenderingEngine engine = new RenderingEngine( renderers );
        final Authorizer authorizer = new Authorizer( config );

        final StaticContentHandler staticContent = new StaticContentHandler( config );

        final Set<RouteHandler> handlers = new HashSet<RouteHandler>()
        {
            private static final long serialVersionUID = 1L;

            {
                add( new GroupContentHandler( store, engine, authorizer ) );
                add( new PageContentHandler( store, engine, authorizer ) );
                add( staticContent );
                add( new UpdateHandler( git ) );
                add( new TemplateContentHandler( new TemplateController( store, config ), serializer ) );
                add( new OfflineContentHandler( store, engine, staticContent ) );
            }
        };

        final ServiceLoader<RouteCollection> collections = ServiceLoader.load( RouteCollection.class );
        final ApplicationRouter router = new ApplicationRouter( handlers, collections );
        router.noMatch( staticContent );

        final String listen = config.getListen();
        vertx.createHttpServer()
             .requestHandler( router )
             .listen( config.getPort(), listen );

        System.out.printf( "Listening for requests on %s:%s\n\n", config.getListen(), config.getPort() );

        synchronized ( this )
        {
            try
            {
                wait();
            }
            catch ( final InterruptedException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
