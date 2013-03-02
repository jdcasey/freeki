package org.commonjava.freemaker.freeki.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.io.IOUtils;
import org.commonjava.web.config.ConfigurationException;
import org.commonjava.web.config.annotation.ConfigName;
import org.commonjava.web.config.io.SingleSectionConfigReader;

@ApplicationScoped
public class FreekiConfig
{

    private static final String CONFIG_FILE_PROPERTY = "freeki.conf";

    private static final File DEFAULT_CONFIG_FILE = new File( System.getProperty( "user.home" ), "freeki.conf" );

    private static final File DEFAULT_STORAGE_DIR = new File( System.getProperty( "user.home" ), "freeki" );

    private File storageDir;

    @PostConstruct
    public void load()
        throws IOException, ConfigurationException
    {
        final String path = System.getProperty( CONFIG_FILE_PROPERTY );
        final File configFile = path == null ? DEFAULT_CONFIG_FILE : new File( path );

        if ( configFile.exists() )
        {
            InputStream stream = null;
            try
            {
                stream = new FileInputStream( configFile );
                new SingleSectionConfigReader( this ).loadConfiguration( stream );
            }
            finally
            {
                IOUtils.closeQuietly( stream );
            }
        }
    }

    public File getStorageDir()
    {
        return storageDir == null ? DEFAULT_STORAGE_DIR : storageDir;
    }

    @ConfigName( "storage.dir" )
    public void setStorageDir( final File storageDir )
    {
        this.storageDir = storageDir;
    }

}
