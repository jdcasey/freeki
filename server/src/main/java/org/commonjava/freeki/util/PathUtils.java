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
package org.commonjava.freeki.util;

import static java.io.File.separator;
import static java.io.File.separatorChar;

public final class PathUtils
{

    private PathUtils()
    {
    }

    public static String buildPath( final String basePath, final String... parts )
    {
        if ( parts == null || parts.length < 1 )
        {
            return basePath;
        }

        final StringBuilder builder = new StringBuilder();

        if ( basePath != null && basePath.length() > 0 && !"/".equals( basePath ) )
        {
            if ( parts[0] == null || !parts[0].startsWith( basePath ) )
            {
                builder.append( basePath );
            }
        }

        for ( String part : parts )
        {
            if ( part == null || part.trim()
                                     .length() < 1 )
            {
                continue;
            }

            if ( part.startsWith( separator ) )
            {
                part = part.substring( 1 );
            }

            if ( builder.length() > 0 && builder.charAt( builder.length() - 1 ) != separatorChar )
            {
                builder.append( separator );
            }

            builder.append( part );
        }

        return builder.toString();
    }

}
