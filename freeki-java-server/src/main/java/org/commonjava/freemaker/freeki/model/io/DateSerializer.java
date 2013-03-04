package org.commonjava.freemaker.freeki.model.io;

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
            return new SimpleDateFormat( DATE_FORMAT ).parse( s );
        }
        catch ( final ParseException e )
        {
            throw new JsonParseException( String.format( "Failed to parse date: '%s'. Reason: %s", s, e.getMessage() ),
                                          e );
        }
    }

    @Override
    public JsonElement serialize( final Date src, final Type type, final JsonSerializationContext ctx )
    {
        return new JsonPrimitive( src == null ? "" : new SimpleDateFormat( DATE_FORMAT ).format( src ) );
    }

}
