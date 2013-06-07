package org.commonjava.freeki.rest;

import static org.commonjava.freeki.rest.PathParameter.DIR;
import static org.commonjava.freeki.rest.PathParameter.PAGE;
import static org.commonjava.freeki.util.ContentType.APPLICATION_JSON;
import static org.commonjava.freeki.util.ContentType.TEXT_HTML;
import static org.commonjava.freeki.util.ContentType.TEXT_PLAIN;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.commonjava.freeki.infra.render.RenderingEngine;
import org.commonjava.freeki.infra.render.RenderingException;
import org.commonjava.freeki.infra.route.Method;
import org.commonjava.freeki.infra.route.RouteHandler;
import org.commonjava.freeki.infra.route.anno.Route;
import org.commonjava.freeki.infra.route.anno.Routes;
import org.commonjava.freeki.model.Page;
import org.commonjava.freeki.store.FreekiStore;
import org.commonjava.freeki.util.ContentType;
import org.commonjava.mimeparse.MIMEParse;
import org.commonjava.util.logging.Logger;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;

public class PageContentHandler
    implements RouteHandler
{

    private static final Set<String> PAGE_ACCEPT = new HashSet<String>()
    {
        private static final long serialVersionUID = 1L;

        {
            add( TEXT_HTML.value() );
            add( TEXT_PLAIN.value() );
            add( APPLICATION_JSON.value() );
        }

    };

    @Inject
    private FreekiStore store;

    @Inject
    private RenderingEngine engine;

    private final Logger logger = new Logger( getClass() );

    public PageContentHandler()
    {
    }

    public PageContentHandler( final FreekiStore store, final RenderingEngine engine )
    {
        this.store = store;
        this.engine = engine;
    }

    /* @formatter:off */
    @Routes( {
       @Route( path="/wiki/:dir=(.*)/:page", method=Method.PUT ),
       @Route( path="/api/page/:page", method=Method.PUT ) 
    } )
    /* @formatter:on */
    public void store( final HttpServerRequest req )
        throws Exception
    {
        String dir = req.params()
                        .get( DIR.param() );

        final String page = req.params()
                               .get( PAGE.param() );

        if ( dir == null )
        {
            dir = "/";
        }

        System.out.printf( "Page: %s\n", page );
        System.out.printf( "Dir: %s\n", dir );

        final StringBuilder content = new StringBuilder();
        req.bodyHandler( new Handler<Buffer>()
        {
            @Override
            public void handle( final Buffer event )
            {
                content.append( new String( event.getBytes() ) );
            }
        } );

        if ( store.storePage( new Page( dir, page, content.toString(), System.currentTimeMillis() ) ) )
        {
            req.response()
               .setStatusCode( 201 )
               .setStatusMessage( "Created: " + page );
        }
        else
        {
            req.response()
               .setStatusCode( 200 )
               .setStatusMessage( "Stored updates to: " + page );
        }

    }

    /* @formatter:off */
    @Routes( {
       @Route( path="/wiki/:dir=(.*)/:page", method=Method.GET ),
       @Route( path="/api/page/:page", method=Method.GET ) 
    } )
    /* @formatter:on */
    public void get( final HttpServerRequest req )
        throws Exception
    {
        final String acceptHeader = req.headers()
                                       .get( "Accept" );
        req.response()
           .setChunked( true );
        req.response()
           .setStatusCode( 200 );

        String dir = req.params()
                        .get( DIR.param() );

        final String page = req.params()
                               .get( PAGE.param() );

        if ( dir == null )
        {
            dir = "/";
        }

        System.out.printf( "Page: %s\n", page );
        System.out.printf( "Dir: %s\n", dir );

        final String mimeAccept = MIMEParse.bestMatch( PAGE_ACCEPT, acceptHeader );
        final ContentType type = ContentType.find( mimeAccept );

        System.out.printf( "Accept header: %s\n", mimeAccept );

        try
        {
            final Page pg = store.getPage( dir, page );
            System.out.printf( "Got page: %s\n", pg );
            final String rendered = engine.render( pg, type );

            System.out.printf( "Rendered to:\n\n%s\n\n", rendered );

            req.response()
               .write( rendered );
        }
        catch ( IOException | RenderingException e )
        {
            logger.error( "Failed to retrieve group: %s. Reason: %s", e, dir, e.getMessage() );
            throw e;
        }
    }

}
