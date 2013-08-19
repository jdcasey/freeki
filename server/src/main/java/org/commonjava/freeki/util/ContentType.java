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
