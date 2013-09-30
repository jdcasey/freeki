/*******************************************************************************
 * Copyright (C) 2013 John Casey.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
