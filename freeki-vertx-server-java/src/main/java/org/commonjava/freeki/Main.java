package org.commonjava.freeki;

import org.commonjava.freeki.rest.ContentHandler;
import org.pegdown.PegDownProcessor;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;
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

    private final ContentHandler handler;

    public Main()
    {
        super();
        start();
        final Vertx v = new DefaultVertx();
        setVertx( v );
        proc = new PegDownProcessor();
        handler = new ContentHandler( proc );
    }

    public void run()
    {
        final RouteMatcher rm = new RouteMatcher();
        rm.get( "/(.+)", handler );

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
