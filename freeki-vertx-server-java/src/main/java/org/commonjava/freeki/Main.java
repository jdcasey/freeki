package org.commonjava.freeki;

import groovy.text.GStringTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import org.commonjava.freeki.conf.FreekiConfig;
import org.commonjava.freeki.conf.GTemplateConfig;
import org.commonjava.freeki.infra.render.ContentRenderer;
import org.commonjava.freeki.infra.render.RenderingEngine;
import org.commonjava.freeki.infra.render.json.JsonRenderer;
import org.commonjava.freeki.infra.render.tmpl.GTHtmlRenderer;
import org.commonjava.freeki.infra.render.tmpl.GTTextRenderer;
import org.commonjava.freeki.infra.route.ApplicationRouter;
import org.commonjava.freeki.infra.route.RouteCollection;
import org.commonjava.freeki.infra.route.RouteHandler;
import org.commonjava.freeki.rest.GroupContentHandler;
import org.commonjava.freeki.rest.PageContentHandler;
import org.commonjava.freeki.store.FreekiStore;
import org.commonjava.freeki.util.ContentType;
import org.commonjava.web.json.ser.JsonSerializer;
import org.pegdown.PegDownProcessor;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.impl.DefaultVertx;
import org.vertx.java.platform.Verticle;

public class Main
    extends Verticle
{

    public static void main( final String[] args )
        throws IOException
    {
        new Main().run();
    }

    private final PegDownProcessor proc;

    private final GStringTemplateEngine templates;

    public Main()
    {
        start();
        final Vertx v = new DefaultVertx();
        setVertx( v );
        proc = new PegDownProcessor();
        templates = new GStringTemplateEngine();
    }

    public void run()
        throws IOException
    {
        final FreekiConfig mainConf = new FreekiConfig( new File( System.getProperty( "user.home" ), "freeki" ) );
        final FreekiStore store = new FreekiStore( mainConf );

        final Map<String, String> rawTemplateConf = new HashMap<>();
        rawTemplateConf.put( "group@" + ContentType.TEXT_HTML.value(), "groovy/group.groovy" );
        rawTemplateConf.put( "page@" + ContentType.TEXT_HTML.value(), "groovy/page.groovy" );
        rawTemplateConf.put( "group@" + ContentType.TEXT_PLAIN.value(), "groovy/group.groovy" );
        rawTemplateConf.put( "page@" + ContentType.TEXT_PLAIN.value(), "groovy/page.groovy" );

        final GTemplateConfig templateConfig = new GTemplateConfig( rawTemplateConf );

        final Set<ContentRenderer> renderers = new HashSet<>();
        renderers.add( new GTHtmlRenderer( templates, proc, templateConfig ) );
        renderers.add( new GTTextRenderer( templates, templateConfig ) );
        renderers.add( new JsonRenderer( new JsonSerializer() ) );

        final RenderingEngine engine = new RenderingEngine( renderers );

        final Set<RouteHandler> handlers = new HashSet<RouteHandler>()
        {
            private static final long serialVersionUID = 1L;

            {
                add( new GroupContentHandler( store, engine ) );
                add( new PageContentHandler( store, engine ) );
            }
        };

        final ServiceLoader<RouteCollection> collections = ServiceLoader.load( RouteCollection.class );
        final ApplicationRouter router = new ApplicationRouter( handlers, collections );

        final HttpServer http = vertx.createHttpServer();

        http.requestHandler( router )
            .listen( 8080, "localhost" );

        System.out.println( "Listening for requests." );

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
