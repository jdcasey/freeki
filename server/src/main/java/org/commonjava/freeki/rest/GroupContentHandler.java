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

import static org.commonjava.freeki.rest.PathParameter.DIR;
import static org.commonjava.freeki.util.ContentType.APPLICATION_JSON;
import static org.commonjava.freeki.util.ContentType.TEXT_HTML;
import static org.commonjava.freeki.util.ContentType.TEXT_PLAIN;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.commonjava.freeki.data.FreekiStore;
import org.commonjava.freeki.infra.auth.Authorizer;
import org.commonjava.freeki.infra.render.RenderingEngine;
import org.commonjava.freeki.infra.render.RenderingException;
import org.commonjava.freeki.model.Group;
import org.commonjava.freeki.util.ContentType;
import org.commonjava.freeki.util.RequestUtils;
import org.commonjava.mimeparse.MIMEParse;
import org.commonjava.util.logging.Logger;
import org.commonjava.vertx.vabr.Method;
import org.commonjava.vertx.vabr.RouteHandler;
import org.commonjava.vertx.vabr.anno.Route;
import org.commonjava.vertx.vabr.anno.Routes;
import org.vertx.java.core.http.HttpServerRequest;

public class GroupContentHandler
    implements RouteHandler
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

    private final FreekiStore store;

    private final RenderingEngine engine;

    private final Logger logger = new Logger( getClass() );

    private final Authorizer auth;

    public GroupContentHandler( final FreekiStore store, final RenderingEngine engine, final Authorizer auth )
    {
        this.store = store;
        this.engine = engine;
        this.auth = auth;
    }

    /* @formatter:off */
    @Routes( { 
        @Route( path = "/api/group/:dir=(.+)", method = Method.DELETE )
    } )
    /* @formatter:on */
    public void delete( final HttpServerRequest req )
        throws Exception
    {
        if ( auth.checkReadOnly( req ) )
        {
            return;
        }

        String dir = req.params()
                        .get( DIR.param() );

        if ( dir == null )
        {
            dir = "/";
        }

        if ( !store.hasGroup( dir ) )
        {
            req.response()
               .setStatusCode( 404 )
               .setStatusMessage( "Not found" );
            return;
        }

        final boolean success = store.deleteGroup( dir );
        if ( success )
        {
            req.response()
               .setStatusCode( 200 )
               .setStatusMessage( "Deleted" )
               .end();
        }
        else
        {
            req.response()
               .setStatusCode( 417 )
               .setStatusMessage( "Delete failed" )
               .end();
        }
    }

    /* @formatter:off */
    @Routes( { 
        @Route( path = "/api/group/:dir=(.+)", method = Method.PUT ),
        @Route( path = "/api/group/:dir=(.+)", method = Method.POST )
    } )
    /* @formatter:on */
    public void store( final HttpServerRequest req )
        throws Exception
    {
        if ( auth.checkReadOnly( req ) )
        {
            return;
        }

        String dir = req.params()
                        .get( DIR.param() );

        if ( dir.endsWith( "/" ) )
        {
            dir = dir.substring( 0, dir.length() - 1 );
        }

        if ( store.storeGroup( new Group( dir ) ) )
        {
            req.response()
               .putHeader( "Location", "/wiki/" + dir + "/" )
               .setStatusCode( 201 )
               .setStatusMessage( "Created: " + dir )
               .end();
        }
        else
        {
            req.response()
               .setStatusCode( 400 )
               .setStatusMessage( "Could not create: " + dir )
               .end();
        }
    }

    /* @formatter:off */
    @Routes( {
        @Route( path="/wiki/:dir=(.+)/", method=Method.GET ), 
        @Route( path="/wiki/", method=Method.GET ), 
        @Route( path="/wiki", method=Method.GET ), 
        @Route( path="/api/group/:dir=(.+)", method=Method.GET ),
    } )
    /* @formatter:on */
    public void get( final HttpServerRequest req )
        throws Exception
    {
        final String acceptHeader = req.headers()
                                       .get( "Accept" );
        req.response()
           .setChunked( true );
        req.response()
           .setStatusCode( 200 );

        String dir = req.params()
                        .get( DIR.param() );

        if ( dir == null )
        {
            dir = "/";
        }
        else if ( dir.endsWith( "/" ) )
        {
            dir = dir.substring( 0, dir.length() - 1 );
        }

        //        System.out.printf( "Directory: %s\n", dir );

        final String mimeAccept = acceptHeader == null ? TEXT_HTML.value() : MIMEParse.bestMatch( DIR_ACCEPT, acceptHeader );
        final ContentType type = ContentType.find( mimeAccept );

        try
        {
            final Group group = store.getGroup( dir );
            if ( group == null )
            {
                req.response()
                   .setStatusCode( 404 )
                   .setStatusMessage( "Not found." )
                   .end();
                return;
            }

            //            logger.info( "Got group with %d children:\n\n  %s\n\n", group.getChildren()
            //                                                                         .size(), join( group.getChildren(), "\n  " ) );

            Map<String, String> queryParams;
            if ( req.query() != null )
            {
                final QueryStringDecoder qsd = new QueryStringDecoder( req.query(), false );
                queryParams = RequestUtils.toMap( qsd.parameters() );
            }
            else
            {
                queryParams = Collections.emptyMap();
            }

            final String rendered = engine.render( group, type, queryParams );

            if ( type != null )
            {
                req.response()
                   .headers()
                   .add( "Content-Type", type.value() );
            }

            if ( rendered != null )
            {
                req.response()
                   .end( rendered );
            }
            else
            {
                req.response()
                   .setStatusCode( 500 )
                   .setStatusMessage( "Rendered group has no content." )
                   .end();
            }
        }
        catch ( IOException | RenderingException e )
        {
            logger.error( "Failed to retrieve group: %s. Reason: %s", e, dir, e.getMessage() );
            throw e;
        }
    }

}
