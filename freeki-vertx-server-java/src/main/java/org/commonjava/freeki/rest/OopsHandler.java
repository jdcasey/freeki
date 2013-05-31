package org.commonjava.freeki.rest;

import groovy.text.GStringTemplateEngine;

import org.pegdown.PegDownProcessor;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

public class OopsHandler
    implements Handler<HttpServerRequest>
{

    private final PegDownProcessor proc;

    private final GStringTemplateEngine templates;

    public OopsHandler( final PegDownProcessor proc, final GStringTemplateEngine templates )
    {
        this.proc = proc;
        this.templates = templates;
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
