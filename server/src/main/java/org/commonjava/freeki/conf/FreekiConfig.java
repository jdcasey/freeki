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
package org.commonjava.freeki.conf;

import java.io.File;
import java.util.regex.Pattern;

import org.kohsuke.args4j.Option;

public class FreekiConfig
{

    private static final String USER_HOME = System.getProperty( "user.home" );

    public static final File DEFAULT_EXPORTS_DIR = new File( USER_HOME, "freeki-exports" );

    public static final File DEFAULT_CONTENT_DIR = new File( USER_HOME, "freeki" );

    public static final String DEFAULT_BRANDING_SUBPATH = ".branding";

    public static final String DEFAULT_TEMPLATE_SUBPATH = ".templates";

    public static final String DEFAULT_STATIC_SUBPATH = "static";

    public static final String DEFAULT_LISTEN = "localhost";

    public static final Integer DEFAULT_PORT = 8080;

    @Option( name = "-h", aliases = "--help", usage = "Show this help screen" )
    private Boolean help;

    @Option( name = "-p", aliases = "--port", usage = "Port to listen on (default: 8080)" )
    private Integer port;

    @Option( name = "-l", aliases = "--listen", usage = "Host or IP address to listen on (default: localhost)" )
    private String listen;

    @Option( name = "-c", aliases = "--content", usage = "Content directory (default: $HOME/freeki)" )
    private String contentDir;

    @Option( name = "-e", aliases = "--exports", usage = "Content directory (default: $HOME/freeki-exports)" )
    private File exportsDir;

    @Option( name = "-b", aliases = "--branding", usage = "Branding content directory (default: $HOME/freeki/.branding)" )
    private File brandingDir;

    @Option( name = "-r", aliases = "--read-only", usage = "Prohibit modification of content; view only." )
    private Boolean readOnly;

    @Option( name = "-s", aliases = "--static", usage = "Static content directory (default: $HOME/freeki/.branding/static)" )
    private File staticDir;

    @Option( name = "-t", aliases = "--templates", usage = "Templates directory (default: $HOME/freeki/.templates)" )
    private File templatesDir;

    @Option( name = "-A", aliases = "--no-autocreate", usage = "Disable auto-creation when links to missing pages are clicked" )
    private Boolean noAutoCreate;

    @Option( name = "-D", aliases = "--debug", usage = "Enable debug options" )
    private Boolean debug;

    @Option( name = "-C", aliases = "--clone", usage = "Clone repo (URL)" )
    private String cloneFrom;

    public FreekiConfig()
    {
    }

    public FreekiConfig( final File storageDir )
    {
        this( storageDir, null, null, null );
    }

    public FreekiConfig( final File contentDir, final File brandingDir, final File staticDir, final File templatesDir )
    {
        this.contentDir = contentDir.getAbsolutePath();
        this.brandingDir = brandingDir;
        this.staticDir = staticDir;
        this.templatesDir = templatesDir;
    }

    public File getContentDir()
    {
        return contentDir == null ? DEFAULT_CONTENT_DIR : new File( eval( contentDir ) );
    }

    private String eval( final String value )
    {
        String result = value.replaceAll( Pattern.quote( "${user.home}" ), System.getProperty( "user.home" ) );
        result = value.replaceAll( "@user.home@?", System.getProperty( "user.home" ) );
        return result;
    }

    public void setContentDir( final File contentDir )
    {
        this.contentDir = contentDir.getAbsolutePath();
    }

    public File getBrandingDir()
    {
        return brandingDir == null ? new File( getContentDir(), DEFAULT_BRANDING_SUBPATH ) : brandingDir;
    }

    public void setBrandingDir( final File brandingDir )
    {
        this.brandingDir = brandingDir;
    }

    public File getTemplatesDir()
    {
        return templatesDir == null ? new File( getContentDir(), DEFAULT_TEMPLATE_SUBPATH ) : templatesDir;
    }

    public void setTemplatesDir( final File templatesDir )
    {
        this.templatesDir = templatesDir;
    }

    public File getStaticDir()
    {
        return staticDir == null ? new File( getBrandingDir(), DEFAULT_STATIC_SUBPATH ) : staticDir;
    }

    public void setStaticDir( final File staticDir )
    {
        this.staticDir = staticDir;
    }

    public boolean isHelp()
    {
        return help == null ? false : help;
    }

    public void setHelp( final boolean help )
    {
        this.help = help;
    }

    public boolean isReadOnly()
    {
        return readOnly == null ? false : readOnly;
    }

    public void setReadOnly( final boolean readOnly )
    {
        this.readOnly = readOnly;
    }

    public String getListen()
    {
        return listen == null ? DEFAULT_LISTEN : listen;
    }

    public void setListen( final String listen )
    {
        this.listen = listen;
    }

    public int getPort()
    {
        return port == null ? DEFAULT_PORT : port;
    }

    public void setPort( final int port )
    {
        this.port = port;
    }

    public boolean isAutoCreate()
    {
        return !noAutoCreate;
    }

    public void setAutoCreate( final boolean autoCreate )
    {
        this.noAutoCreate = !autoCreate;
    }

    public boolean isDebug()
    {
        return debug;
    }

    public void setDebug( final boolean debug )
    {
        this.debug = debug;
    }

    public File getExportsDir()
    {
        return exportsDir == null ? DEFAULT_EXPORTS_DIR : exportsDir;
    }

    public void setExportsDir( final File exportsDir )
    {
        this.exportsDir = exportsDir;
    }

    public void overrideWith( final FreekiConfig overrides )
    {
        if ( overrides.help != null )
        {
            this.help = overrides.help;
        }

        if ( overrides.brandingDir != null )
        {
            this.brandingDir = overrides.brandingDir;
        }

        if ( overrides.contentDir != null )
        {
            this.contentDir = overrides.contentDir;
        }

        if ( overrides.listen != null )
        {
            this.listen = overrides.listen;
        }

        if ( overrides.port != null )
        {
            this.port = overrides.port;
        }

        if ( overrides.staticDir != null )
        {
            this.staticDir = overrides.staticDir;
        }

        if ( overrides.templatesDir != null )
        {
            this.templatesDir = overrides.templatesDir;
        }

        if ( overrides.readOnly != null )
        {
            this.readOnly = overrides.readOnly;
        }

        if ( overrides.noAutoCreate != null )
        {
            this.noAutoCreate = overrides.noAutoCreate;
        }

        if ( overrides.exportsDir != null )
        {
            this.exportsDir = overrides.exportsDir;
        }

        if ( overrides.debug != null )
        {
            this.debug = overrides.debug;
        }

        if ( overrides.cloneFrom != null )
        {
            this.cloneFrom = overrides.cloneFrom;
        }
    }

    public String getCloneFrom()
    {
        return cloneFrom;
    }

    public void setCloneFrom( final String cloneFrom )
    {
        this.cloneFrom = cloneFrom;
    }

}
