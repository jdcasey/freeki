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
