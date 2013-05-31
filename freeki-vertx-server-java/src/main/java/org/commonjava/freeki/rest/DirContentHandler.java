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
import java.util.HashSet;
import java.util.Set;

import org.commonjava.freeki.routing.Method;
import org.commonjava.freeki.routing.Route;
import org.commonjava.mimeparse.MIMEParse;
import org.pegdown.PegDownProcessor;
import org.vertx.java.core.http.HttpServerRequest;

public class DirContentHandler
    implements Route
{

    private static final Set<String> DIR_ACCEPT = new HashSet<String>()
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

    public DirContentHandler( final PegDownProcessor proc, final GStringTemplateEngine templates )
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

        String dir = req.params()
                        .get( "dir" );
        if ( dir == null )
        {
            dir = "/";
        }

        System.out.printf( "Directory: %s\n", dir );

        final String mimeAccept = MIMEParse.bestMatch( DIR_ACCEPT, acceptHeader );
        System.out.printf( "Accept header: %s\n", mimeAccept );

        req.response()
           .write( proc.markdownToHtml( "# Listing for `" + dir
                       + "`\n  - file.md\n  - file2.md\n\n\n\n\n## MIME Type: " + mimeAccept ) );
    }

    @Override
    public Iterable<String> patterns()
    {
        return Arrays.asList( "/:dir=(.+)/", "/" );
    }

    @Override
    public Iterable<Method> methods()
    {
        return Arrays.asList( GET, POST, PUT, DELETE, HEAD );
    }

}
