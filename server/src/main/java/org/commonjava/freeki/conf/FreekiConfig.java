package org.commonjava.freeki.conf;

import java.io.File;

public class FreekiConfig
{

    private static final String USER_HOME = System.getProperty( "user.home" );

    public static final File DEFAULT_STORAGE_DIR = new File( USER_HOME, "freeki" );

    public static final String DEFAULT_BRANDING_SUBPATH = ".branding";

    public static final String DEFAULT_TEMPLATE_SUBPATH = ".templates";

    private File storageDir;

    private File brandingDir;

    private File templatesDir;

    public FreekiConfig()
    {
    }

    public FreekiConfig( final File storageDir )
    {
        this( storageDir, null, null );
    }

    public FreekiConfig( final File storageDir, final File brandingDir, final File templatesDir )
    {
        this.storageDir = storageDir;
        this.brandingDir = brandingDir;
        this.templatesDir = templatesDir;
    }

    public File getStorageDir()
    {
        return storageDir == null ? DEFAULT_STORAGE_DIR : storageDir;
    }

    public void setStorageDir( final File storageDir )
    {
        this.storageDir = storageDir;
    }

    public File getBrandingDir()
    {
        return brandingDir == null ? new File( getStorageDir(), DEFAULT_BRANDING_SUBPATH ) : brandingDir;
    }

    public void setBrandingDir( final File brandingDir )
    {
        this.brandingDir = brandingDir;
    }

    public File getTemplatesDir()
    {
        return templatesDir == null ? new File( getStorageDir(), DEFAULT_TEMPLATE_SUBPATH ) : templatesDir;
    }

    public void setTemplatesDir( final File templatesDir )
    {
        this.templatesDir = templatesDir;
    }

}
