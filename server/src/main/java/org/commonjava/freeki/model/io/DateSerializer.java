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
package org.commonjava.freeki.model.io;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.commonjava.web.json.ser.WebSerializationAdapter;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateSerializer
    implements WebSerializationAdapter, JsonSerializer<Date>, JsonDeserializer<Date>
{

    public static final String DATE_FORMAT = "yyyy-MM-dd kk:mm Z";

    @Override
    public void register( final GsonBuilder gsonBuilder )
    {
        gsonBuilder.registerTypeAdapter( Date.class, this );
    }

    @Override
    public Date deserialize( final JsonElement src, final Type type, final JsonDeserializationContext ctx )
        throws JsonParseException
    {
        final String s = src.getAsString();
        if ( s.trim()
              .length() < 1 )
        {
            return null;
        }

        try
        {
            return parseDate( s );
        }
        catch ( final ParseException e )
        {
            throw new JsonParseException( String.format( "Failed to parse date: '%s'. Reason: %s", s, e.getMessage() ),
                                          e );
        }
    }

    public static Date parseDate( final String s )
        throws ParseException
    {
        return new SimpleDateFormat( DATE_FORMAT ).parse( s );
    }

    @Override
    public JsonElement serialize( final Date src, final Type type, final JsonSerializationContext ctx )
    {
        return new JsonPrimitive( src == null ? "" : formatDate( src ) );
    }

    public static String formatDate( final Date src )
    {
        return new SimpleDateFormat( DATE_FORMAT ).format( src );
    }

}
