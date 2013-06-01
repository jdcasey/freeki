package org.commonjava.freeki.util;

public enum ContentType
{
    TEXT_HTML( "text/html" ), TEXT_PLAIN( "text/plain" ), APPLICATION_JSON( "application/json" );

    private String rawType;

    private ContentType( final String rawType )
    {
        this.rawType = rawType;
    }

    public static ContentType find( final String type )
    {
        for ( final ContentType ct : values() )
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
