package org.commonjava.freeki.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.commonjava.web.config.ConfigurationException;
import org.commonjava.web.config.annotation.ConfigName;
import org.commonjava.web.config.annotation.SectionName;
import org.commonjava.web.config.dotconf.DotConfConfigurationReader;

@ApplicationScoped
@SectionName( "main" )
public class FreekiConfig
{

    private static final String CONFIG_FILE_PROPERTY = "freeki.conf";

    private static final File DEFAULT_CONFIG_FILE = new File( System.getProperty( "user.home" ), "freeki.conf" );

    private static final File DEFAULT_STORAGE_DIR = new File( System.getProperty( "user.home" ), "freeki" );

    private File storageDir;

    @Inject
    private Instance<FreekiConfigSection> sections;

    public FreekiConfig()
    {
    }

    public FreekiConfig( final File storageDir )
    {
        this.storageDir = storageDir;
    }

    @PostConstruct
    public void load()
        throws IOException, ConfigurationException
    {
        final Set<Object> sections = new HashSet<Object>();
        sections.add( this );
        for ( final FreekiConfigSection section : this.sections )
        {
            sections.add( section );
        }

        final String path = System.getProperty( CONFIG_FILE_PROPERTY );
        final File configFile = path == null ? DEFAULT_CONFIG_FILE : new File( path );

        if ( configFile.exists() )
        {
            InputStream stream = null;
            try
            {
                stream = new FileInputStream( configFile );
                new DotConfConfigurationReader( sections.toArray( new Object[sections.size()] ) ).loadConfiguration( stream );
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
