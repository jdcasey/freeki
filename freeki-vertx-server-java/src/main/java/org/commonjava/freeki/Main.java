package org.commonjava.freeki;

import groovy.text.GStringTemplateEngine;

import org.commonjava.freeki.rest.DirContentHandler;
import org.commonjava.freeki.rest.OopsHandler;
import org.commonjava.freeki.rest.PageContentHandler;
import org.commonjava.freeki.route.FreekiRouteMatcher;
import org.pegdown.PegDownProcessor;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.impl.DefaultVertx;
import org.vertx.java.platform.Verticle;

public class Main
    extends Verticle
{

    public static void main( final String[] args )
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
    {
        final FreekiRouteMatcher rm = new FreekiRouteMatcher().add( new DirContentHandler( proc, templates ) )
                                                              .add( new PageContentHandler( proc, templates ) )
                                                              .noMatch( new OopsHandler( proc, templates ) );

        final HttpServer http = vertx.createHttpServer();
        http.requestHandler( rm )
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
