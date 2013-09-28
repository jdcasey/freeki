package org.commonjava.freeki.util;

import static java.io.File.separator;
import static java.io.File.separatorChar;

public final class PathUtils
{

    private PathUtils()
    {
    }

    public static String buildPath( final String basePath, final String... parts )
    {
        if ( parts == null || parts.length < 1 )
        {
            return basePath;
        }

        final StringBuilder builder = new StringBuilder();

        if ( basePath != null && basePath.length() > 0 && !"/".equals( basePath ) )
        {
            if ( parts[0] == null || !parts[0].startsWith( basePath ) )
            {
                builder.append( basePath );
            }
        }

        for ( String part : parts )
        {
            if ( part == null || part.trim()
                                     .length() < 1 )
            {
                continue;
            }

            if ( part.startsWith( separator ) )
            {
                part = part.substring( 1 );
            }

            if ( builder.length() > 0 && builder.charAt( builder.length() - 1 ) != separatorChar )
            {
                builder.append( separator );
            }

            builder.append( part );
        }

        return builder.toString();
    }

}
