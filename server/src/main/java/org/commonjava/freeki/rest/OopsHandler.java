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

import org.commonjava.util.logging.Logger;
import org.pegdown.PegDownProcessor;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

public class OopsHandler
    implements Handler<HttpServerRequest>
{

    private final Logger logger = new Logger( getClass() );

    private final PegDownProcessor proc;

    public OopsHandler( final PegDownProcessor proc )
    {
        this.proc = proc;
    }

    @Override
    public void handle( final HttpServerRequest req )
    {
        req.response()
           .setChunked( true );
        req.response()
           .setStatusCode( 404 )
           .setStatusMessage( "Not found" );

        logger.warn( "OOPS: %s\n", req.uri() );

        req.response()
           .end( proc.markdownToHtml( "# NOT FOUND " + req.uri() ) );
    }

}
