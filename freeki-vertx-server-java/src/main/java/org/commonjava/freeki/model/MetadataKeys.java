package org.commonjava.freeki.model;

enum MetadataKeys
{
    CREATED, CURRENT_AUTHOR, TITLE;

    public static MetadataKeys metadataKey( final String name )
    {
        return valueOf( name );
    }
}