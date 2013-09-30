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
package org.commonjava.freeki.testutil;

import java.io.File;
import java.net.URL;

public class FileFixture
{

    public File getClasspathFile( final String resource )
    {
        final URL url = Thread.currentThread()
                              .getContextClassLoader()
                              .getResource( resource );
        if ( url == null )
        {
            return null;
        }

        return new File( url.getPath() );
    }

}
