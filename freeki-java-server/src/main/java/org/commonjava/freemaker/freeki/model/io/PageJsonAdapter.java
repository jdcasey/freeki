package org.commonjava.freemaker.freeki.model.io;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

import org.commonjava.freemaker.freeki.model.Page;
import org.commonjava.web.json.ser.WebSerializationAdapter;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PageJsonAdapter
    implements WebSerializationAdapter, JsonSerializer<Page>, JsonDeserializer<Page>
{

    @Override
    public void register( final GsonBuilder gsonBuilder )
    {
        gsonBuilder.registerTypeHierarchyAdapter( Page.class, this );
    }

    @Override
    public Page deserialize( final JsonElement src, final Type type, final JsonDeserializationContext ctx )
        throws JsonParseException
    {
        final JsonObject object = src.getAsJsonObject();

        final JsonElement contentEl = object.remove( "content" );
        final String contentEnc = contentEl.getAsString();

        final byte[] decoded = decodeBase64( contentEnc );
        object.addProperty( "content", new String( decoded, Charset.forName( "UTF-8" ) ) );

        final Page pg = ctx.deserialize( object, type );
        pg.syncContentWithTitle();

        return pg;
    }

    @Override
    public JsonElement serialize( final Page src, final Type type, final JsonSerializationContext ctx )
    {
        src.syncContentWithTitle();

        final JsonElement element = ctx.serialize( src, type );
        final JsonObject object = element.getAsJsonObject();

        final String contentDec = object.remove( "content" )
                                        .getAsString();

        object.addProperty( "content", encodeBase64String( contentDec.getBytes( Charset.forName( "UTF-8" ) ) ) );

        return object;
    }

}
