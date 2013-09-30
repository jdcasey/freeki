package org.commonjava.freeki.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vertx.java.core.MultiMap;

public final class RequestUtils
{

    private RequestUtils()
    {
    }

    public static Map<String, String> toMap( final MultiMap mm )
    {
        final Map<String, String> map = new HashMap<String, String>();
        for ( final String name : mm.names() )
        {
            map.put( name, mm.get( name ) );
        }

        return map;
    }

    public static Map<String, String> toMap( final Map<String, List<String>> mm )
    {
        final Map<String, String> map = new HashMap<String, String>();
        for ( final String name : mm.keySet() )
        {
            final List<String> vals = mm.get( name );
            if ( vals == null || vals.isEmpty() )
            {
                map.put( name, "true" );
            }
            else
            {
                map.put( name, vals.get( 0 ) );
            }
        }

        return map;
    }

}
