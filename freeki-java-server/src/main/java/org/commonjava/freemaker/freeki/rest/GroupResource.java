package org.commonjava.freemaker.freeki.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.commonjava.freemaker.freeki.model.Group;
import org.commonjava.freemaker.freeki.store.FreekiStore;
import org.commonjava.util.logging.Logger;
import org.commonjava.web.json.model.Listing;
import org.commonjava.web.json.ser.JsonSerializer;

@Path( "/groups" )
public class GroupResource
{

    private final Logger logger = new Logger( getClass() );

    @Inject
    private FreekiStore store;

    @Inject
    private JsonSerializer serializer;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path( "/{path : (.+/)?}@" )
    public Listing<Group> list( @PathParam( "path" ) final String path )
        throws IOException
    {
        final List<Group> groups = new ArrayList<Group>();
        final Set<Group> list = store.listGroups( path );
        if ( list == null || list.isEmpty() )
        {
            throw new WebApplicationException( Status.NOT_FOUND );
        }
        else
        {
            groups.addAll( list );
        }

        return new Listing<Group>( groups );
    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path( "/{path : (.+)}" )
    public Group get( @PathParam( "path" ) final String path )
        throws IOException
    {
        final Group grp = store.getGroup( path );
        if ( grp == null )
        {
            throw new WebApplicationException( Status.NOT_FOUND );
        }

        return grp;
    }

    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    public Response create( final Group group, @Context final HttpServletRequest request )
        throws IOException
    {
        store.storeGroup( group );

        return Response.status( Status.CREATED )
                       .entity( group )
                       .build();
    }

    @DELETE
    @Path( "/{group : (.+/)?}@" )
    public Response deleteGroup( @PathParam( "group" ) final String group )
    {
        Response response;
        try
        {
            if ( store.delete( group, null ) )
            {
                response = Response.ok()
                                   .build();
            }
            else
            {
                response = Response.status( Status.NOT_FOUND )
                                   .build();
            }
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
