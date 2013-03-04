package org.commonjava.freemaker.freeki.inject;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

import org.commonjava.web.json.ser.JsonSerializer;

@RequestScoped
public class SerializerProvider
{

    private JsonSerializer serializer;

    @Produces
    public JsonSerializer getSerializer()
    {
        if ( serializer == null )
        {
            serializer = new JsonSerializer();
        }

        return serializer;
    }

}
