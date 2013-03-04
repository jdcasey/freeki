package org.commonjava.freemaker.freeki.model.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.commonjava.freemaker.freeki.model.Page;
import org.commonjava.web.json.ser.JsonSerializer;

@Provider
@Consumes( MediaType.APPLICATION_JSON )
@Produces( MediaType.APPLICATION_JSON )
public class PageMessageProvider
    implements MessageBodyReader<Page>, MessageBodyWriter<Page>
{

    @Inject
    private JsonSerializer serializer;

    @Override
    public Page readFrom( final Class<Page> type, final Type genericType, final Annotation[] annotations,
                          final MediaType mediaType, final MultivaluedMap<String, String> httpHeaders,
                          final InputStream entityStream )
        throws IOException, WebApplicationException
    {
        return serializer.fromStream( entityStream, "UTF-8", Page.class );
    }

    @Override
    public void writeTo( final Page t, final Class<?> type, final Type genericType, final Annotation[] annotations,
                         final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders,
                         final OutputStream entityStream )
        throws IOException, WebApplicationException
    {
        entityStream.write( serializer.toString( t )
                                      .getBytes( "UTF-8" ) );
    }

    @Override
    public boolean isWriteable( final Class<?> type, final Type genericType, final Annotation[] annotations,
                                final MediaType mediaType )
    {
        return Page.class.isAssignableFrom( type );
    }

    @Override
    public long getSize( final Page t, final Class<?> type, final Type genericType, final Annotation[] annotations,
                         final MediaType mediaType )
    {
        // TODO
        return -1;
    }

    @Override
    public boolean isReadable( final Class<?> type, final Type genericType, final Annotation[] annotations,
                               final MediaType mediaType )
    {
        return Page.class.isAssignableFrom( type );
    }

}
