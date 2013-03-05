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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.commonjava.freemaker.freeki.model.Group;
import org.commonjava.freemaker.freeki.store.FreekiStore;
import org.commonjava.util.logging.Logger;
import org.commonjava.web.json.model.Listing;
import org.commonjava.web.json.ser.JsonSerializer;

@Path( "/group" )
public class GroupResource
{

    private final Logger logger = new Logger( getClass() );

    @Inject
    private FreekiStore store;

    @Inject
    private JsonSerializer serializer;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    @Path( "/{sub: (.+)?}" )
    public Listing<Group> getAll( @PathParam( "sub" ) final String subgroup )
    {
        final List<Group> groups = new ArrayList<Group>( store.listGroups( subgroup ) );
        Collections.sort( groups );

        return new Listing<Group>( groups );
    }

    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    public Response create( final Group group, @Context final HttpServletRequest request )
        throws IOException
    {
        store.storeGroup( group );

        return Response.ok()
                       .build();
    }

    @DELETE
    @Path( "/{group: (.+)}" )
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
