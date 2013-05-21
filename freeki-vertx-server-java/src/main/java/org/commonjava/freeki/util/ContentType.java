package org.commonjava.freeki.util;

public enum ContentType
{
    TEXT_HTML( "text/html" ), TEXT_PLAIN( "text/plain" ), APPLICATION_JSON( "application/json" );

    private String rawType;

    private ContentType( final String rawType )
    {
        this.rawType = rawType;
    }

    public String value()
    {
        return rawType;
    }

}
