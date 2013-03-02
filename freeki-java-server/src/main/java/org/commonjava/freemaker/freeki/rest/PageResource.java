package org.commonjava.freemaker.freeki.rest;

import java.io.IOException;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.commonjava.freemaker.freeki.model.Page;
import org.commonjava.freemaker.freeki.store.FreekiStore;
import org.commonjava.util.logging.Logger;
import org.commonjava.web.json.model.Listing;
import org.commonjava.web.json.ser.JsonSerializer;

@Path( "/wiki" )
public class PageResource
{

    private final Logger logger = new Logger( getClass() );

    @Inject
    private FreekiStore store;

    @Inject
    private JsonSerializer serializer;

    @GET
    public Response listGroups()
    {
        final Set<String> groups = store.listGroups();
        return Response.ok( serializer.toString( new Listing<String>( groups ) ) )
                       .build();
    }

    @GET
    @Path( "/{group}" )
    public Response list( @PathParam( "group" ) final String group )
    {
        Response response = Response.status( Status.NOT_FOUND )
                                    .build();
        if ( store.hasGroup( group ) )
        {
            final Set<String> metadata = store.list( group );
            response = Response.ok( serializer.toString( new Listing<String>( metadata ) ) )
                               .build();
        }

        return response;
    }

    @GET
    @Path( "/{group}/{title}" )
    public Response get( @PathParam( "group" ) final String group, @PathParam( "title" ) final String title )
    {
        Response response = Response.status( Status.NOT_FOUND )
                                    .build();

        if ( store.hasPage( group, title ) )
        {
            try
            {
                final Page pg = store.getPage( group, title );
                response = Response.ok( serializer.toString( pg ) )
                                   .build();
            }
            catch ( final IOException e )
            {
                logger.error( "Failed to retrieve page: %s in group: %s.\nReason: %s", e, title, group, e.getMessage() );
                response = Response.serverError()
                                   .build();
            }
        }

        return response;
    }

    @POST
    @Path( "/{group}" )
    public Response store( @PathParam( "group" ) final String group, @Context final HttpServletRequest request )
    {
        Response response = Response.status( Status.BAD_REQUEST )
                                    .build();
        try
        {
            Page pg = null;
            try
            {
                pg = serializer.fromRequestBody( request, Page.class );
            }
            catch ( final RuntimeException e )
            {
                logger.error( "Failed to store page for group: %s from request.\nReason: %s", e, group, e.getMessage() );
            }

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
    @Path( "/{group}/{title}" )
    public Response update( @PathParam( "group" ) final String group, @PathParam( "title" ) final String title,
                            @Context final HttpServletRequest request )
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
                Page pg = null;
                try
                {
                    pg = serializer.fromRequestBody( request, Page.class );
                }
                catch ( final RuntimeException e )
                {
                    logger.error( "Failed to store page for group: %s from request.\nReason: %s", e, group,
                                  e.getMessage() );
                }

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
    @Path( "/{group}/{title}" )
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

    @DELETE
    @Path( "/{group}" )
    public Response deleteGroup( @PathParam( "group" ) final String group )
    {
        Response response;
        try
        {
            store.delete( group, null );
            response = Response.ok()
                               .build();
        }
        catch ( final IOException e )
        {
            logger.error( "Failed to delete group: %s. Reason: %s", e, group, e.getMessage() );
            response = Response.serverError()
                               .build();
        }

        return response;
    }

}
