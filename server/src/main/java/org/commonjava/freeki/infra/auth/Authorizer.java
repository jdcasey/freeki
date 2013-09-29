package org.commonjava.freeki.infra.auth;

import org.commonjava.freeki.conf.FreekiConfig;
import org.vertx.java.core.http.HttpServerRequest;

public class Authorizer
{

    private final FreekiConfig config;

    public Authorizer( final FreekiConfig config )
    {
        this.config = config;
    }

    public boolean checkReadOnly( final HttpServerRequest req )
    {
        if ( config.isReadOnly() )
        {
            req.response()
               .setStatusCode( 400 )
               .setStatusMessage( "This service is running in read-only mode." )
               .end();
            return true;
        }

        return false;
    }

}
