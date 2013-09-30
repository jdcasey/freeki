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
