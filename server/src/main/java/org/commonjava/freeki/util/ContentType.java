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

public final class ContentType
{
    public static final ContentType APPLICATION_JSON = new ContentType( "application/json" );

    public static final ContentType TEXT_HTML = new ContentType( "text/html" );

    public static final ContentType TEXT_PLAIN = new ContentType( "text/plain" );

    public static final ContentType[] KNOWN = { APPLICATION_JSON, TEXT_HTML, TEXT_PLAIN };

    private final String rawType;

    public ContentType( final String rawType )
    {
        this.rawType = rawType;
    }

    public static ContentType find( final String type )
    {
        for ( final ContentType ct : KNOWN )
        {
            if ( ct.rawType.equalsIgnoreCase( type ) )
            {
                return ct;
            }
        }

        return null;
    }

    public String value()
    {
        return rawType;
    }

}
