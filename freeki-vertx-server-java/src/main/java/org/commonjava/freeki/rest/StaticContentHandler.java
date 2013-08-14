package org.commonjava.freeki.rest;

import static org.commonjava.freeki.rest.PathParameter.PATH;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.commonjava.freeki.conf.FreekiConfig;
import org.commonjava.freeki.infra.route.Method;
import org.commonjava.freeki.infra.route.RouteHandler;
import org.commonjava.freeki.infra.route.anno.Route;
import org.commonjava.freeki.infra.route.anno.Routes;
import org.commonjava.util.logging.Logger;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;

public class StaticContentHandler
    implements RouteHandler
{

    private final Logger logger = new Logger( getClass() );

    private final File staticBasedir;

    public StaticContentHandler( final FreekiConfig mainConf )
    {
        this.staticBasedir = mainConf.getStaticDir();
    }

    /* @formatter:off */
    @Routes( {
        @Route( path="/static/:path=(.+)", method=Method.GET )
    } )
    /* @formatter:on */
    public void get( final HttpServerRequest req )
        throws Exception
    {
        req.response()
           .setChunked( true );

        req.response()
           .setStatusCode( 200 );

        final String path = req.params()
                               .get( PATH.param() );

        logger.info( "Serving static path: '%s'", path );

        final File f = new File( staticBasedir, path );
        if ( !f.exists() )
        {
            final String resource = "static/" + path;
            logger.info( "Path does not exist in static directory: %s; checking for classpath resource: '%s'",
                         staticBasedir, resource );

            final InputStream stream = Thread.currentThread()
                                             .getContextClassLoader()
                                             .getResourceAsStream( resource );
            if ( stream == null )
            {
                logger.info( "No dice" );
                req.response()
                   .setStatusMessage( "Not found" )
                   .setStatusCode( 404 );
            }
            else
            {
                logger.info( "Sending classpath resource: %s", resource );
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                IOUtils.copy( stream, baos );

                try
                {
                    final int len = baos.toByteArray().length;
                    final Buffer buf = new Buffer( baos.toByteArray() );
                    logger.info( "Send: %d bytes", len );
                    req.response()
                       .headers()
                       .add( "Content-Length", Integer.toString( len ) );

                    req.response()
                       .write( buf );
                }
                finally
                {
                    IOUtils.closeQuietly( stream );
                }
            }
        }
        else if ( f.isDirectory() )
        {
            logger.info( "Requested file is actually a directory!" );
            req.response()
               .setStatusMessage( "Content is a directory" )
               .setStatusCode( 404 );
        }
        else
        {
            logger.info( "Sending freeki file resource: %s", f.getAbsolutePath() );
            req.response()
               .sendFile( f.getAbsolutePath() );
        }
    }
}
