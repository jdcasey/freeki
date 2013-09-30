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
package org.commonjava.freeki.infra.render.json;

import java.util.Map;

import org.commonjava.freeki.infra.render.ContentRenderer;
import org.commonjava.freeki.util.ContentType;
import org.commonjava.web.json.ser.JsonSerializer;

public class JsonRenderer
    implements ContentRenderer
{

    private static final ContentType[] TYPES = { ContentType.APPLICATION_JSON };

    private final JsonSerializer serializer;

    public JsonRenderer( final JsonSerializer serializer )
    {
        this.serializer = serializer;
    }

    @Override
    public ContentType[] getContentTypes()
    {
        return TYPES;
    }

    @Override
    public String render( final Object data, final Map<String, String> requestParams )
    {
        return serializer.toString( data );
    }

}
