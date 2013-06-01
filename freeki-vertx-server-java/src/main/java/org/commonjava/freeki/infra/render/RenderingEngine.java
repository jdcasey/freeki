package org.commonjava.freeki.infra.render;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.commonjava.freeki.util.ContentType;
import org.commonjava.util.logging.Logger;

public class RenderingEngine
{

    private final Logger logger = new Logger( getClass() );

    private final Map<ContentType, ContentRenderer> renderers = new HashMap<>();

    @Inject
    private Instance<ContentRenderer> injectedRenderers;

    public RenderingEngine()
    {
    }

    public RenderingEngine( final Iterable<ContentRenderer> renderers )
    {
        mapRenderers( renderers );
    }

    @PostConstruct
    public void injectRenderers()
    {
        mapRenderers( injectedRenderers );
    }

    private void mapRenderers( final Iterable<ContentRenderer> rs )
    {
        for ( final ContentRenderer r : rs )
        {
            for ( final ContentType t : r.getContentTypes() )
            {
                renderers.put( t, r );
            }
        }
    }

    public String render( final Object data, final ContentType type )
        throws RenderingException
    {
        if ( data == null )
        {
            return null;
        }

        final ContentRenderer renderer = renderers.get( type );
        System.out.printf( "Using renderer: %s for data: %s with content-type: %s\n", renderer, data, type );
        if ( renderer != null )
        {
            return renderer.render( data );
        }

        throw new RenderingException( "Cannot find renderer for content type: %s", type.value() );
    }

}
