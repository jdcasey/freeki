package org.commonjava.freeki.rest;

import static org.commonjava.freeki.util.ContentType.APPLICATION_JSON;
import static org.commonjava.freeki.util.ContentType.TEXT_HTML;
import static org.commonjava.freeki.util.ContentType.TEXT_PLAIN;

import java.util.HashSet;
import java.util.Set;

import org.commonjava.mimeparse.MIMEParse;
import org.pegdown.PegDownProcessor;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

public class DirContentHandler
    implements Handler<HttpServerRequest>
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

    public DirContentHandler( final PegDownProcessor proc )
    {
        this.proc = proc;
    }

    @Override
    public void handle( final HttpServerRequest req )
    {
        final String acceptHeader = req.headers()
                                       .get( "Accept" );
        req.response()
           .setChunked( true );
        req.response()
           .setStatusCode( 200 );

        System.out.printf( "Directory: %s\n", req.uri() );

        final String mimeAccept = MIMEParse.bestMatch( DIR_ACCEPT, acceptHeader );
        System.out.printf( "Accept header: %s\n", mimeAccept );

        req.response()
           .write( proc.markdownToHtml( "# Listing for " + req.uri() + "\n  - file.md\n  - file2.md\n" ) );
    }

}
