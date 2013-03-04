package org.commonjava.freemaker.freeki.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.commonjava.freemaker.freeki.store.FreekiStore;
import org.commonjava.util.logging.Logger;
import org.commonjava.web.json.model.Listing;

@Path( "/" )
public class GroupResource
{

    private final Logger logger = new Logger( getClass() );

    @Inject
    private FreekiStore store;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Listing<String> getAll()
    {
        final List<String> groups = new ArrayList<String>( store.listGroups() );
        Collections.sort( groups );

        return new Listing<String>( groups );
    }

    @POST
    public Response create( @QueryParam( "group" ) final String group )
    {
        store.storeGroup( group );

        return Response.ok()
                       .build();
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
