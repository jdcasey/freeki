package org.commonjava.freeki.rest;

import static org.commonjava.freeki.routing.Method.DELETE;
import static org.commonjava.freeki.routing.Method.GET;
import static org.commonjava.freeki.routing.Method.HEAD;
import static org.commonjava.freeki.routing.Method.POST;
import static org.commonjava.freeki.routing.Method.PUT;
import static org.commonjava.freeki.util.ContentType.APPLICATION_JSON;
import static org.commonjava.freeki.util.ContentType.TEXT_HTML;
import static org.commonjava.freeki.util.ContentType.TEXT_PLAIN;
import groovy.text.GStringTemplateEngine;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.commonjava.freeki.routing.Method;
import org.commonjava.freeki.routing.Route;
import org.commonjava.mimeparse.MIMEParse;
import org.pegdown.PegDownProcessor;
import org.vertx.java.core.http.HttpServerRequest;

public class PageContentHandler
    implements Route
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

    private final PegDownProcessor proc;

    private final GStringTemplateEngine templates;

    public PageContentHandler( final PegDownProcessor proc, final GStringTemplateEngine templates )
    {
        this.proc = proc;
        this.templates = templates;
    }

    @Override
    public void handle( final Method method, final HttpServerRequest req )
    {
        final String acceptHeader = req.headers()
                                       .get( "Accept" );
        req.response()
           .setChunked( true );
        req.response()
           .setStatusCode( 200 );

        System.out.printf( "Page: %s\n", req.params()
                                            .get( "page" ) );
        System.out.printf( "Dir: %s\n", req.params()
                                           .get( "dir" ) );

        final String mimeAccept = MIMEParse.bestMatch( PAGE_ACCEPT, acceptHeader );
        System.out.printf( "Accept header: %s\n", mimeAccept );

        req.response()
           .write( proc.markdownToHtml( "# Page " + req.params()
                                                       .get( "page" ) + "\n\n  * Directory: " + req.params()
                                                                                                   .get( "dir" ) ) );
        req.response()
           .end();
    }

    @Override
    public Iterable<String> patterns()
    {
        return Collections.singleton( "/:dir=(.+)/:page" );
    }

    @Override
    public Iterable<Method> methods()
    {
        return Arrays.asList( GET, POST, PUT, DELETE, HEAD );
    }

}
