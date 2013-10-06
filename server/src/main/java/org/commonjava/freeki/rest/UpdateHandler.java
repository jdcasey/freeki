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

import java.io.IOException;
import java.util.Date;

import org.commonjava.freeki.data.GitManager;
import org.commonjava.util.logging.Logger;
import org.commonjava.vertx.vabr.Method;
import org.commonjava.vertx.vabr.RouteHandler;
import org.commonjava.vertx.vabr.anno.Route;
import org.commonjava.vertx.vabr.anno.Routes;
import org.vertx.java.core.http.HttpServerRequest;

public class UpdateHandler
    implements RouteHandler
{

    private final Logger logger = new Logger( getClass() );

    private final GitManager git;

    public UpdateHandler( final GitManager git )
    {
        this.git = git;
    }

    /* @formatter:off */
    @Routes( {
        @Route( path="/update/content", method=Method.POST ),
        @Route( path="/update/pull", method=Method.POST )
    } )
    /* @formatter:on */
    public void pull( final HttpServerRequest req )
    {
        try
        {
            git.pullUpdates();
            req.response()
               .end();
        }
        catch ( final IOException e )
        {
            logger.error( "Failed to pull updates: %s", e, e.getMessage() );
            req.response()
               .setStatusCode( 500 )
               .setStatusMessage( "Failed to pull content: " + e.getMessage() )
               .end();
        }
    }

    /* @formatter:off */
    @Routes( {
        @Route( path="/update/push", method=Method.POST )
    } )
    /* @formatter:on */
    public void push( final HttpServerRequest req )
    {
        final String user = req.headers()
                               .get( "user" );

        final String password = req.headers()
                                   .get( "password" );

        try
        {
            git.pushUpdates( user, password );
            req.response()
               .end();

            logger.info( "Pushed content at: %s", new Date() );
        }
        catch ( final IOException e )
        {
            logger.error( "Failed to push updates: %s", e, e.getMessage() );
            req.response()
               .setStatusCode( 500 )
               .setStatusMessage( "Failed to push content: " + e.getMessage() )
               .end();
        }
    }

}
