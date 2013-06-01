package org.commonjava.freeki.infra.render;

import org.commonjava.freeki.FreekiException;

public class RenderingException
    extends FreekiException
{

    private static final long serialVersionUID = 1L;

    public RenderingException( final String format, final Object... params )
    {
        super( format, params );
    }

    public RenderingException( final String format, final Throwable error, final Object... params )
    {
        super( format, error, params );
    }

}
