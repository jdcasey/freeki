package org.commonjava.freeki.rest;

import org.pegdown.PegDownProcessor;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

public class ContentHandler
    implements Handler<HttpServerRequest>
{

    private final DirContentHandler dir;

    private final PageContentHandler page;

    public ContentHandler( final PegDownProcessor proc )
    {
        this.dir = new DirContentHandler( proc );
        this.page = new PageContentHandler( proc );
    }

    @Override
    public void handle( final HttpServerRequest req )
    {
        if ( req.uri()
                .endsWith( "/" ) )
        {
            dir.handle( req );
        }
        else
        {
            page.handle( req );
        }

        req.response()
           .end();
    }

}
