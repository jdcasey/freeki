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

import org.commonjava.freeki.infra.anno.RenderKey;

public final class RenderUtils
{

    private RenderUtils()
    {
    }

    public static String getTemplateKey( final Object data )
        throws RenderingException
    {
        final RenderKey rk = data.getClass()
                                 .getAnnotation( RenderKey.class );

        String key = null;
        if ( rk != null )
        {
            key = rk.key();
        }

        if ( key == null )
        {
            throw new RenderingException( "Cannot render. No @RenderKey annotation found on type: %s", data.getClass()
                                                                                                           .getName() );
        }

        return key;
    }

    public static String getContentTemplate( final Object data )
        throws RenderingException
    {
        final RenderKey rk = data.getClass()
                                 .getAnnotation( RenderKey.class );

        String key = null;
        if ( rk != null )
        {
            key = rk.contentTemplate();
        }

        if ( key == null )
        {
            throw new RenderingException( "Cannot render. No @RenderKey annotation found on type: %s", data.getClass()
                                                                                                           .getName() );
        }

        return key;
    }

}
