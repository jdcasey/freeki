package org.commonjava.freeki.infra.render;

import org.commonjava.freeki.infra.anno.RenderKey;

public final class RenderUtils
{

    private RenderUtils()
    {
    }

    public static String getTemplateKey( final Object data )
        throws RenderingException
    {
        final RenderKey rk = data.getClass()
                                 .getAnnotation( RenderKey.class );

        String key = null;
        if ( rk != null )
        {
            key = rk.key();
        }

        if ( key == null )
        {
            throw new RenderingException( "Cannot render. No @RenderKey annotation found on type: %s", data.getClass()
                                                                                                           .getName() );
        }

        return key;
    }

    public static String getContentTemplate( final Object data )
        throws RenderingException
    {
        final RenderKey rk = data.getClass()
                                 .getAnnotation( RenderKey.class );

        String key = null;
        if ( rk != null )
        {
            key = rk.contentTemplate();
        }

        if ( key == null )
        {
            throw new RenderingException( "Cannot render. No @RenderKey annotation found on type: %s", data.getClass()
                                                                                                           .getName() );
        }

        return key;
    }

}