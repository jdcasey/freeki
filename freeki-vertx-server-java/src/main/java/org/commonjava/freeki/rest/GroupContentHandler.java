package org.commonjava.freeki.rest;

import static org.apache.commons.lang.StringUtils.join;
import static org.commonjava.freeki.infra.route.Method.DELETE;
import static org.commonjava.freeki.infra.route.Method.GET;
import static org.commonjava.freeki.infra.route.Method.HEAD;
import static org.commonjava.freeki.infra.route.Method.POST;
import static org.commonjava.freeki.infra.route.Method.PUT;
import static org.commonjava.freeki.util.ContentType.APPLICATION_JSON;
import static org.commonjava.freeki.util.ContentType.TEXT_HTML;
import static org.commonjava.freeki.util.ContentType.TEXT_PLAIN;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.commonjava.freeki.infra.render.RenderingEngine;
import org.commonjava.freeki.infra.render.RenderingException;
import org.commonjava.freeki.infra.route.Method;
import org.commonjava.freeki.infra.route.Route;
import org.commonjava.freeki.model.Group;
import org.commonjava.freeki.store.FreekiStore;
import org.commonjava.freeki.util.ContentType;
import org.commonjava.mimeparse.MIMEParse;
import org.commonjava.util.logging.Logger;
import org.vertx.java.core.http.HttpServerRequest;

public class GroupContentHandler
    implements Route
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

    @Inject
    private FreekiStore store;

    @Inject
    private RenderingEngine engine;

    private final Logger logger = new Logger( getClass() );

    public GroupContentHandler()
    {
    }

    public GroupContentHandler( final FreekiStore store, final RenderingEngine engine )
    {
        this.store = store;
        this.engine = engine;
    }

    @Override
    public void handle( final Method method, final HttpServerRequest req )
        throws Exception
    {
        final String acceptHeader = req.headers()
                                       .get( "Accept" );
        req.response()
           .setChunked( true );
        req.response()
           .setStatusCode( 200 );

        String dir = req.params()
                        .get( "dir" );
        if ( dir == null )
        {
            dir = "/";
        }

        System.out.printf( "Directory: %s\n", dir );

        final String mimeAccept = MIMEParse.bestMatch( DIR_ACCEPT, acceptHeader );
        final ContentType type = ContentType.find( mimeAccept );

        try
        {
            final Group group = store.getGroup( dir );
            System.out.printf( "Got group with %d children:\n\n  %s\n\n", group.getChildren()
                                                                               .size(),
                               join( group.getChildren(), "\n  " ) );
            final String rendered = engine.render( group, type );

            req.response()
               .write( rendered );
        }
        catch ( IOException | RenderingException e )
        {
            logger.error( "Failed to retrieve group: %s. Reason: %s", e, dir, e.getMessage() );
            throw e;
        }
    }

    @Override
    public Iterable<String> patterns()
    {
        return Arrays.asList( "/:dir=(.+)/", "/" );
    }

    @Override
    public Iterable<Method> methods()
    {
        return Arrays.asList( GET, POST, PUT, DELETE, HEAD );
    }

}
