package org.commonjava.freemaker.freeki.store;

import static org.apache.commons.io.FileUtils.forceDelete;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FileUtils.write;
import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.commonjava.freemaker.freeki.conf.FreekiConfig;
import org.commonjava.freemaker.freeki.model.Page;
import org.commonjava.util.logging.Logger;
import org.commonjava.web.json.ser.JsonSerializer;

public class FreekiStore
{

    private final Logger logger = new Logger( getClass() );

    @Inject
    private FreekiConfig config;

    @Inject
    private JsonSerializer serializer;

    public boolean hasGroup( final String group )
    {
        final File root = config.getStorageDir();
        final File d = new File( root, group );

        return d.isDirectory();
    }

    public boolean hasPage( final String group, final String title )
    {
        final File file = getFile( group, title );
        return file.isFile();
    }

    public Set<String> listPages( final String group )
    {
        final File root = config.getStorageDir();
        final File d = new File( root, group );

        final Set<String> result = new HashSet<String>();
        if ( d.isDirectory() )
        {
            final File[] files = d.listFiles( new FilenameFilter()
            {
                @Override
                public boolean accept( final File dir, final String name )
                {
                    return dir.equals( d ) && name.endsWith( ".md" );
                }
            } );

            for ( final File file : files )
            {
                BufferedReader br = null;
                try
                {
                    br = new BufferedReader( new FileReader( file ) );
                    final String title = Page.readTitle( br );
                    if ( title != null )
                    {
                        result.add( title );
                    }
                }
                catch ( final IOException e )
                {
                    logger.error( "Cannot read: '%s'. Reason: %s", e, file, e.getMessage() );
                }
                finally
                {
                    closeQuietly( br );
                }
            }
        }

        return result;
    }

    public void storeGroup( final String group )
    {
        final File groupDir = getFile( group, null );
        if ( !groupDir.exists() )
        {
            groupDir.mkdirs();
        }
    }

    public void storePage( final Page page )
        throws IOException
    {
        final File pageFile = getFile( page.getGroup(), page.getTitle() );
        final File dir = pageFile.getParentFile();

        if ( !dir.isDirectory() && !dir.mkdirs() )
        {
            throw new IOException( "Failed to create directory structure for page: " + dir.getAbsolutePath() );
        }

        write( pageFile, page.render() );
    }

    private File getFile( final String group, final String title )
    {
        final File root = config.getStorageDir();
        final File groupDir = new File( root, group );
        return title == null ? groupDir : new File( groupDir, pageFilename( title ) + ".md" );
    }

    private String pageFilename( final String title )
    {
        return title.toLowerCase()
                    .replaceAll( "[^-_a-zA-Z0-9]+", "-" );
    }

    public Page getPage( final String group, final String title )
        throws IOException
    {
        final File file = getFile( group, title );
        final String content = readFileToString( file );

        return new Page( content, file.lastModified() );
    }

    public void delete( final String group, final String title )
        throws IOException
    {
        File file = getFile( group, title );
        if ( file == null )
        {
            file = new File( config.getStorageDir(), group );
        }

        if ( file.isDirectory() )
        {
            forceDelete( file );
            file = file.getParentFile();
        }
        else
        {
            file.delete();
            file = file.getParentFile();
        }

        while ( file.isDirectory() && !config.getStorageDir()
                                             .equals( file ) && file.list().length < 1 )
        {
            file.delete();
            file = file.getParentFile();
        }
    }

    public Set<String> listGroups()
    {
        final File root = config.getStorageDir();
        final String[] names = root.isDirectory() ? root.list() : new String[] {};
        final Set<String> groups = new HashSet<String>( names.length );
        for ( final String name : names )
        {
            final File f = new File( root, name );
            if ( f.isDirectory() )
            {
                groups.add( name );
            }
        }

        return groups;
    }

}
