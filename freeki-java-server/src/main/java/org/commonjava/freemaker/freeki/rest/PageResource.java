package org.commonjava.freemaker.freeki.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.commonjava.freemaker.freeki.model.Page;
import org.commonjava.freemaker.freeki.store.FreekiStore;
import org.commonjava.util.logging.Logger;
import org.commonjava.web.json.model.Listing;
import org.commonjava.web.json.ser.JsonSerializer;

@Path( "/page/{group : .+}" )
public class PageResource
{

    private final Logger logger = new Logger( getClass() );

    @Inject
    private FreekiStore store;

    @Inject
    private JsonSerializer serializer;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Listing<String> list( @PathParam( "group" ) final String group )
    {
        if ( store.hasGroup( group ) )
        {
            final List<String> pages = new ArrayList<String>( store.listPages( group ) );
            Collections.sort( pages );

            return new Listing<String>( pages );
        }

        return null;
    }

    @GET
    @Path( "/@/{title}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Page get( @PathParam( "group" ) final String group, @PathParam( "title" ) final String title )
    {
        if ( store.hasPage( group, title ) )
        {
            try
            {
                final Page pg = store.getPage( group, title );
                return pg;
            }
            catch ( final IOException e )
            {
                logger.error( "Failed to retrieve page: %s in group: %s.\nReason: %s", e, title, group, e.getMessage() );
            }
        }

        return null;
    }

    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    public Response store( final Page pg, @PathParam( "group" ) final String group,
                           @Context final HttpServletRequest request )
    {
        Response response = Response.status( Status.BAD_REQUEST )
                                    .build();
        try
        {
            //            Page pg = null;
            //            try
            //            {
            //                pg = serializer.fromRequestBody( request, Page.class );
            //            }
            //            catch ( final RuntimeException e )
            //            {
            //                logger.error( "Failed to store page for group: %s from request.\nReason: %s", e, group, e.getMessage() );
            //            }

            if ( pg != null )
            {
                pg.setGroup( group );

                store.storePage( pg );
                response = Response.ok()
                                   .build();
            }
        }
        catch ( final IOException e )
        {
            logger.error( "Failed to store page for group: %s.\nReason: %s", e, group, e.getMessage() );
            response = Response.serverError()
                               .build();
        }

        return response;
    }

    @PUT
    @Path( "/@/{title}" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response update( final Page pg, @PathParam( "group" ) final String group,
                            @PathParam( "title" ) final String title, @Context final HttpServletRequest request )
    {
        Response response = Response.status( Status.BAD_REQUEST )
                                    .build();
        try
        {
            final Page orig = store.getPage( group, title );
            if ( orig == null )
            {
                response = Response.status( Status.NOT_FOUND )
                                   .build();
            }
            else
            {
                //                Page pg = null;
                //                try
                //                {
                //                    pg = serializer.fromRequestBody( request, Page.class );
                //                }
                //                catch ( final RuntimeException e )
                //                {
                //                    logger.error( "Failed to store page for group: %s from request.\nReason: %s", e, group,
                //                                  e.getMessage() );
                //                }

                if ( pg != null )
                {
                    if ( orig.updateFrom( pg ) )
                    {
                        store.storePage( orig );
                        response = Response.ok()
                                           .build();
                    }
                }
            }
        }
        catch ( final IOException e )
        {
            logger.error( "Failed to store page for group: %s.\nReason: %s", e, group, e.getMessage() );
            response = Response.serverError()
                               .build();
        }

        return response;
    }

    @DELETE
    @Path( "/@/{title}" )
    public Response delete( @PathParam( "group" ) final String group, @PathParam( "title" ) final String title )
    {
        Response response;
        try
        {
            store.delete( group, title );
            response = Response.ok()
                               .build();
        }
        catch ( final IOException e )
        {
            logger.error( "Failed to delete (or possibly prune): %s in %s. Reason: %s", e, title, group, e.getMessage() );
            response = Response.serverError()
                               .build();
        }

        return response;
    }

}
