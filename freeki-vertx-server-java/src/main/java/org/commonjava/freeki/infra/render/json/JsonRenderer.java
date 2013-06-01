package org.commonjava.freeki.infra.render.json;

import javax.inject.Inject;

import org.commonjava.freeki.infra.render.ContentRenderer;
import org.commonjava.freeki.util.ContentType;
import org.commonjava.web.json.ser.JsonSerializer;

public class JsonRenderer
    implements ContentRenderer
{

    private static final ContentType[] TYPES = { ContentType.APPLICATION_JSON };

    @Inject
    private JsonSerializer serializer;

    public JsonRenderer()
    {
    }

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
    public String render( final Object data )
    {
        return serializer.toString( data );
    }

}
