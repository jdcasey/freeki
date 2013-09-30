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
package org.commonjava.freeki.infra.render;

import java.util.HashMap;
import java.util.Map;

import org.commonjava.freeki.util.ContentType;

public class RenderingEngine
{

    private final Map<ContentType, ContentRenderer> renderers = new HashMap<>();

    public RenderingEngine()
    {
    }

    public RenderingEngine( final Iterable<ContentRenderer> renderers )
    {
        mapRenderers( renderers );
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

    public String render( final Object data, final ContentType type, final Map<String, String> requestParams )
        throws RenderingException
    {
        if ( data == null )
        {
            return null;
        }

        final ContentRenderer renderer = renderers.get( type );
        //        System.out.printf( "Using renderer: %s for data: %s with content-type: %s\n", renderer, data, type );
        if ( renderer != null )
        {
            return renderer.render( data, requestParams );
        }

        throw new RenderingException( "Cannot find renderer for content type: %s", type );
    }

}
