package org.commonjava.freeki.rest;

import org.commonjava.util.logging.Logger;
import org.pegdown.PegDownProcessor;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

public class OopsHandler
    implements Handler<HttpServerRequest>
{

    private final Logger logger = new Logger( getClass() );

    private final PegDownProcessor proc;

    public OopsHandler( final PegDownProcessor proc )
    {
        this.proc = proc;
    }

    @Override
    public void handle( final HttpServerRequest req )
    {
        req.response()
           .setChunked( true );
        req.response()
           .setStatusCode( 404 )
           .setStatusMessage( "Not found" );

        logger.warn( "OOPS: %s\n", req.uri() );

        req.response()
           .end( proc.markdownToHtml( "# NOT FOUND " + req.uri() ) );
    }

}
