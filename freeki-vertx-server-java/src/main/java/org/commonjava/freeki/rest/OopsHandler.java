package org.commonjava.freeki.rest;

import org.pegdown.PegDownProcessor;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

public class OopsHandler
    implements Handler<HttpServerRequest>
{

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
           .setStatusCode( 200 );

        System.out.printf( "OOPS: %s\n", req.uri() );

        req.response()
           .write( proc.markdownToHtml( "# NOT FOUND " + req.uri() ) );
    }

}
