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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.commonjava.freeki.data.TemplateController;
import org.commonjava.freeki.data.TemplateException;
import org.commonjava.vertx.vabr.Method;
import org.commonjava.vertx.vabr.RouteHandler;
import org.commonjava.vertx.vabr.anno.Route;
import org.commonjava.vertx.vabr.anno.Routes;
import org.commonjava.web.json.model.Listing;
import org.commonjava.web.json.ser.JsonSerializer;
import org.vertx.java.core.VoidHandler;
import org.vertx.java.core.http.HttpServerRequest;

public class TemplateContentHandler
    implements RouteHandler
{

    //    private final Logger logger = new Logger( getClass() );

    private final TemplateController controller;

    private final JsonSerializer serializer;

    public TemplateContentHandler( final TemplateController controller, final JsonSerializer serializer )
    {
        this.controller = controller;
        this.serializer = serializer;
    }

    /* @formatter:off */
    @Routes( {
        @Route( path="/templates", method=Method.GET )
    } )
    /* @formatter:on */
    public void list( final HttpServerRequest req )
        throws Exception
    {
        final List<String> labels = controller.getTemplateLabels();

        final String json = serializer.toString( new Listing<String>( labels ) );

        req.response()
           .setStatusCode( 200 )
           .putHeader( "Content-Length", Integer.toString( json.length() ) )
           .write( json )
           .end();
    }

    /* @formatter:off */
    @Routes( {
        @Route( path="/templates/:template", method=Method.GET )
    } )
    /* @formatter:on */
    public void get( final HttpServerRequest req )
        throws Exception
    {
        final String template = req.params()
                                   .get( PathParameter.TEMPLATE.param() );

        //        logger.info( "Loading HTML form for template: '%s'\nURI: '%s'", template, req.absoluteURI() );
        final File html = controller.getTemplateHtml( template );
        if ( !html.exists() )
        {
            req.response()
               .setStatusCode( 404 )
               .setStatusMessage( "Not Found" )
               .end();
        }
        else
        {
            req.response()
               .setStatusCode( 200 )
               .sendFile( html.getAbsolutePath() );
        }
    }

    /* @formatter:off */
    @Routes( {
        @Route( path="/templates/:template", method=Method.POST )
    } )
    /* @formatter:on */
    public void post( final HttpServerRequest req )
        throws Exception
    {
        req.expectMultiPart( true );

        req.endHandler( new VoidHandler()
        {
            @Override
            protected void handle()
            {
                final String template = req.params()
                                           .get( PathParameter.TEMPLATE.param() );

                final Map<String, String> params = new HashMap<>();
                for ( final Map.Entry<String, String> entry : req.params() )
                {
                    final String key = entry.getKey();
                    if ( !params.containsKey( key ) )
                    {
                        //                        logger.info( "PARAMS+ (path) %s = %s", key, entry.getValue() );
                        params.put( key, entry.getValue() );
                    }
                }

                for ( final Map.Entry<String, String> entry : req.formAttributes() )
                {
                    final String key = entry.getKey();
                    if ( !params.containsKey( key ) )
                    {
                        //                        logger.info( "PARAMS+ (form) %s = %s", key, entry.getValue() );
                        params.put( key, entry.getValue() );
                    }
                }

                try
                {
                    final String location = controller.runTemplateAction( template, params );
                    if ( location == null )
                    {
                        req.response()
                           .setStatusCode( 200 )
                           .setStatusMessage( "OK" )
                           .end();
                    }
                    else
                    {
                        req.response()
                           .setStatusCode( 302 )
                           .putHeader( "Location", location )
                           .end();
                    }
                }
                catch ( final TemplateException e )
                {
                    req.response()
                       .setStatusCode( 500 )
                       .setStatusMessage( e.getMessage() )
                       .end();
                }
            }
        } );
    }

}
