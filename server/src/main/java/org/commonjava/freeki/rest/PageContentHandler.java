package org.commonjava.freeki.rest;

import static org.commonjava.freeki.rest.PathParameter.DIR;
import static org.commonjava.freeki.rest.PathParameter.PAGE;
import static org.commonjava.freeki.util.ContentType.APPLICATION_JSON;
import static org.commonjava.freeki.util.ContentType.TEXT_HTML;
import static org.commonjava.freeki.util.ContentType.TEXT_PLAIN;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.commonjava.freeki.data.FreekiStore;
import org.commonjava.freeki.infra.render.RenderingEngine;
import org.commonjava.freeki.infra.render.RenderingException;
import org.commonjava.freeki.infra.route.Method;
import org.commonjava.freeki.infra.route.RouteBinding;
import org.commonjava.freeki.infra.route.RouteHandler;
import org.commonjava.freeki.infra.route.anno.Route;
import org.commonjava.freeki.infra.route.anno.Routes;
import org.commonjava.freeki.model.Page;
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

    private final FreekiStore store;

    private final RenderingEngine engine;

    private final Logger logger = new Logger( getClass() );

    public PageContentHandler( final FreekiStore store, final RenderingEngine engine )
    {
        this.store = store;
        this.engine = engine;
    }

    /* @formatter:off */
    @Routes( {
       @Route( path="/api/page/:dir=(.*)/:page", method=Method.DELETE ) 
    } )
    /* @formatter:on */
    public void delete( final HttpServerRequest req )
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

        if ( !store.hasPage( dir, page ) )
        {
            req.response()
               .setStatusCode( 404 )
               .setStatusMessage( "Not found" )
               .end();
            return;
        }

        final boolean success = store.deletePage( dir, page );
        if ( success )
        {
            req.response()
               .setStatusCode( 200 )
               .setStatusMessage( "Deleted" )
               .end();
        }
        else
        {
            req.response()
               .setStatusCode( 417 )
               .setStatusMessage( "Delete failed" )
               .end();
        }
    }

    /* @formatter:off */
    @Routes( {
       @Route( path="/api/page/:page", method=Method.PUT, contentType="text/plain" ), 
       @Route( path="/api/page/:page", method=Method.POST, contentType="text/plain" ), 
       @Route( path="/api/page/:dir=(.*)/:page", method=Method.PUT, contentType="text/plain" ), 
       @Route( path="/api/page/:dir=(.*)/:page", method=Method.POST, contentType="text/plain" ) 
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

        //        logger.info( "Page: %s\n", page );
        //        logger.info( "Dir: %s\n", dir );

        final String group = dir;

        req.bodyHandler( new Handler<Buffer>()
        {
            @Override
            public void handle( final Buffer event )
            {
                final String content = event.getString( 0, event.length() );
                try
                {
                    //                    logger.info( "Received content:\n\n%s\n\n", content );

                    String title = page;
                    boolean titleParsed = false;

                    final BufferedReader reader = new BufferedReader( new StringReader( content.toString() ) );
                    String firstLine = null;
                    do
                    {
                        firstLine = reader.readLine();
                    }
                    while ( firstLine.trim()
                                     .length() < 1 );

                    if ( firstLine != null && firstLine.length() > 0 )
                    {
                        if ( firstLine.startsWith( "# " ) )
                        {
                            title = firstLine.substring( 2 );
                            titleParsed = true;
                        }
                        else
                        {
                            final String secondLine = reader.readLine();
                            if ( secondLine != null && secondLine.matches( "[=]+" ) )
                            {
                                title = firstLine;
                                titleParsed = true;
                            }
                        }
                    }

                    //                    logger.info( "Using title: %s", title );

                    Page pageObj = store.getPage( group, page );
                    if ( pageObj == null )
                    {
                        pageObj =
                            new Page( group, Page.serverPathFor( group, page ), content.toString(), title, System.currentTimeMillis(), "unknown" );
                    }
                    else
                    {
                        //                        logger.info( "Setting content:\n\n%s\n\n", content );
                        pageObj.setContent( content.toString() );
                        pageObj.setUpdated( new Date() );
                        //                        if ( pageObj.getTitle() == null )
                        //                        {
                        if ( titleParsed )
                        {
                            pageObj.setTitle( title );
                            //                        }
                        }
                    }

                    final String location = "/wiki/" + pageObj.getId();
                    //                    logger.info( "Setting Location header to: '%s'", location );
                    if ( store.storePage( pageObj ) )
                    {
                        req.response()
                           .putHeader( "Location", location )
                           .setStatusCode( 201 )
                           .setStatusMessage( "Created: " + page )
                           .end();
                    }
                    else
                    {
                        req.response()
                           .putHeader( "Location", location )
                           .setStatusCode( 200 )
                           .setStatusMessage( "Stored updates to: " + page )
                           .end();
                    }
                }
                catch ( final Exception e )
                {
                    logger.error( e.getMessage(), e );
                    req.response()
                       .setStatusCode( 500 )
                       .setStatusMessage( e.getMessage() )
                       .end( e.getMessage() );
                }
                finally
                {
                }
            }
        } );
    }

    /* @formatter:off */
    @Routes( {
       @Route( path="/wiki/:page", method=Method.GET ),
       @Route( path="/wiki/:dir=(.*)/:page", method=Method.GET ),
       @Route( path="/api/page/:page", method=Method.GET, contentType="text/plain" ), 
       @Route( path="/api/page/:dir=(.*)/:page", method=Method.GET, contentType="text/plain" ) 
    } )
    /* @formatter:on */
    public void get( final HttpServerRequest req )
        throws Exception
    {
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

        logger.info( "Page: %s\n", page );
        logger.info( "Dir: %s\n", dir );

        String mimeAccept = req.headers()
                               .get( RouteBinding.RECOMMENDED_CONTENT_TYPE );
        //        logger.info( "Recommended content type: %s", mimeAccept );

        if ( mimeAccept == null )
        {
            final String acceptHeader = req.headers()
                                           .get( "Accept" );

            mimeAccept = MIMEParse.bestMatch( PAGE_ACCEPT, acceptHeader );
        }

        final ContentType type = ContentType.find( mimeAccept );

        //        logger.info( "Using content type: %s\n", type );

        try
        {
            final Page pg = store.getPage( dir, page );
            //            logger.info( "Got page: %s\n", pg );
            final String rendered = engine.render( pg, type );

            //            logger.info( "Rendered to:\n\n%s\n\n", rendered );

            req.response()
               .end( rendered );
        }
        catch ( IOException | RenderingException e )
        {
            logger.error( "Failed to retrieve group: %s. Reason: %s", e, dir, e.getMessage() );
            throw e;
        }
    }

}
