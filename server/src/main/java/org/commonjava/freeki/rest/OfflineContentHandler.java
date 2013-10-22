package org.commonjava.freeki.rest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.commonjava.freeki.data.FreekiStore;
import org.commonjava.freeki.infra.render.RenderingEngine;
import org.commonjava.freeki.model.ChildRef;
import org.commonjava.freeki.model.ChildRef.ChildType;
import org.commonjava.freeki.model.Page;
import org.commonjava.freeki.util.ContentType;
import org.commonjava.util.logging.Logger;
import org.commonjava.vertx.vabr.Method;
import org.commonjava.vertx.vabr.RouteHandler;
import org.commonjava.vertx.vabr.anno.Route;
import org.commonjava.vertx.vabr.anno.Routes;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;

public class OfflineContentHandler
    implements RouteHandler
{

    private final FreekiStore store;

    private final RenderingEngine engine;

    private final Logger logger = new Logger( getClass() );

    private final StaticContentHandler staticContent;

    public OfflineContentHandler( final FreekiStore store, final RenderingEngine engine, final StaticContentHandler staticContent )
    {
        this.store = store;
        this.engine = engine;
        this.staticContent = staticContent;
    }

    /* @formatter:off */
    @Routes( {
       @Route( path="/offline/zip", method=Method.GET, contentType="application/zip" ),
    } )
    /* @formatter:on */
    public void get( final HttpServerRequest req )
        throws Exception
    {
        req.endHandler( new Handler<Void>()
        {

            @Override
            public void handle( final Void event )
            {
                try
                {
                    final Set<String> groups = new TreeSet<>();
                    final Set<Page> pages = new TreeSet<>();

                    final LinkedList<ChildRef> todo = new LinkedList<>( store.listChildren( "/" ) );
                    while ( !todo.isEmpty() )
                    {
                        final ChildRef next = todo.removeFirst();
                        if ( next.getType() == ChildType.GROUP )
                        {
                            final String groupId = next.getId();
                            groups.add( groupId );
                            final SortedSet<ChildRef> nextChildren = store.listChildren( groupId );

                            logger.info( "Found group: %s with children:\n\n", next.getId(), nextChildren );
                            if ( nextChildren != null )
                            {
                                todo.addAll( nextChildren );
                            }
                        }
                        else
                        {
                            final File f = new File( next.getId() );

                            logger.info( "Found page: %s (group: %s, page: %s)", next.getId(), f.getParent(), f.getName() );

                            final Page page = store.getPage( f.getParent(), f.getName() );
                            if ( page != null )
                            {
                                pages.add( page );
                            }
                        }
                    }

                    // TODO: Store in a temp file, then find a way to invalidate older copies of the zip file.
                    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    final ZipOutputStream zos = new ZipOutputStream( baos );
                    for ( final String group : groups )
                    {
                        zos.putNextEntry( new ZipEntry( group + "/" ) );
                        logger.info( "Adding group directory: %s", group );
                        zos.closeEntry();
                    }

                    final Map<String, String> params = new HashMap<>();
                    for ( final Page page : pages )
                    {
                        logger.info( "Rendering page: %s", page );
                        final String content = engine.render( page, ContentType.STATIC_HTML, params );
                        zos.putNextEntry( new ZipEntry( page.getId() ) );
                        logger.info( "writing page:\n\n%s\n\n", content );
                        zos.write( content.getBytes() );
                        zos.closeEntry();
                    }

                    zos.flush();
                    zos.close();

                    // TODO: Find a way to enumerate all the static resources (js, css, images, etc.)
                    req.response()
                       .headers()
                       .add( "Content-Type", "application/zip" )
                       .add( "Content-Length", Integer.toString( baos.size() ) );

                    logger.info( "Sending zip to client." );
                    req.response()
                       .setChunked( true )
                       .setStatusCode( 200 )
                       .write( new Buffer( baos.toByteArray() ) )
                       .end();
                }
                catch ( final Exception e )
                {
                    logger.error( "Failed to generate offline content archive. Reason: %s", e, e.getMessage() );
                    req.response()
                       .setStatusCode( 500 )
                       .setStatusMessage( "Error generating offline content: " + e.getMessage() )
                       .end();
                }
            }

        } );
    }
}
