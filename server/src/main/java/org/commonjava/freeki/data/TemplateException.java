package org.commonjava.freeki.data;

import org.commonjava.freeki.FreekiException;

public class TemplateException
    extends FreekiException
{

    private static final long serialVersionUID = 1L;

    public TemplateException( final String format, final Object... params )
    {
        super( format, params );
    }

    public TemplateException( final String format, final Throwable error, final Object... params )
    {
        super( format, error, params );
    }

}
