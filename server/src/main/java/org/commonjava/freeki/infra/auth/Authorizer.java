/*******************************************************************************
 * Copyright (C) 2013 John Casey.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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

    public boolean checkAutoCreate()
    {
        return !config.isReadOnly() && config.isAutoCreate();
    }

}
