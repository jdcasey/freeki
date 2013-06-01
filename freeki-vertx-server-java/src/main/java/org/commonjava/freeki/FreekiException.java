package org.commonjava.freeki;

import java.util.IllegalFormatException;

public abstract class FreekiException
    extends Exception
{

    private static final long serialVersionUID = 1L;

    private final Object[] params;

    protected FreekiException( final String format, final Throwable error, final Object... params )
    {
        super( format, error );
        this.params = params;
    }

    protected FreekiException( final String format, final Object... params )
    {
        super( format );
        this.params = params;
    }

    @Override
    public String getMessage()
    {
        final String message = super.getMessage();
        if ( params != null && params.length > 0 )
        {
            try
            {
                return String.format( message, params );
            }
            catch ( final IllegalFormatException e )
            {
            }
        }

        return message;
    }

    @Override
    public String getLocalizedMessage()
    {
        return getMessage();
    }

}
