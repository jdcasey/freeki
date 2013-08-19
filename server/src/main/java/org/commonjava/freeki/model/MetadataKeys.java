package org.commonjava.freeki.model;

enum MetadataKeys
{
    CREATED, CURRENT_AUTHOR, TITLE;

    public static MetadataKeys metadataKey( final String name )
    {
        for ( final MetadataKeys k : values() )
        {
            if ( k.name()
                  .equalsIgnoreCase( name ) )
            {
                return k;
            }
        }

        return null;
    }
}