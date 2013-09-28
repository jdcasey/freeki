package org.commonjava.freeki.rest;

import org.commonjava.freeki.data.FreekiStore;
import org.commonjava.freeki.infra.route.Method;
import org.commonjava.freeki.infra.route.RouteHandler;
import org.commonjava.freeki.infra.route.anno.Route;
import org.commonjava.freeki.infra.route.anno.Routes;
import org.vertx.java.core.http.HttpServerRequest;

public class UpdateNotificationHandler
    implements RouteHandler
{

    private final FreekiStore store;

    public UpdateNotificationHandler( final FreekiStore store )
    {
        this.store = store;
    }

    /* @formatter:off */
    @Routes( {
        @Route( path="/update/content", method=Method.POST )
    } )
    /* @formatter:on */
    public void get( final HttpServerRequest req )
        throws Exception
    {
        req.response()
           .setStatusCode( 200 );

        store.pullUpdates();

        req.response()
           .end();
    }
}
