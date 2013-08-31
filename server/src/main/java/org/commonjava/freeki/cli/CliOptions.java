package org.commonjava.freeki.cli;

import java.io.File;

import org.commonjava.freeki.conf.FreekiConfig;
import org.kohsuke.args4j.Option;

public class CliOptions
{

    @Option( name = "-p", aliases = "--port", usage = "Port to listen on (default: 8080)" )
    private int port = 8080;

    @Option( name = "-l", aliases = "--listen", usage = "Host or IP address to listen on (default: localhost)" )
    private String listen = "localhost";

    @Option( name = "-c", aliases = "--content", usage = "Content directory (default: $HOME/freeki)" )
    private File contentDir = FreekiConfig.DEFAULT_STORAGE_DIR;

    @Option( name = "-h", aliases = "--help", usage = "Show this help screen" )
    private boolean help;

    @Option( name = "-b", aliases = "--branding", usage = "Branding content directory (default: $HOME/freeki/.branding)" )
    private File brandingDir;

    public int getPort()
    {
        return port;
    }

    public File getContentDir()
    {
        return contentDir;
    }

    public void setPort( final int port )
    {
        this.port = port;
    }

    public void setContentDir( final File contentDir )
    {
        this.contentDir = contentDir;
    }

    public boolean isHelp()
    {
        return help;
    }

    public void setHelp( final boolean help )
    {
        this.help = help;
    }

    public String getListen()
    {
        return listen;
    }

    public void setListen( final String listen )
    {
        this.listen = listen;
    }

    public File getBrandingDir()
    {
        return brandingDir;
    }

    public void setBrandingDir( final File brandingDir )
    {
        this.brandingDir = brandingDir;
    }

}
