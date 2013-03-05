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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.commonjava.freemaker.freeki.conf.FreekiConfig;
import org.commonjava.freemaker.freeki.model.Group;
import org.commonjava.freemaker.freeki.model.Page;
import org.commonjava.util.logging.Logger;
import org.commonjava.web.json.ser.JsonSerializer;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RmCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class FreekiStore
{

    private static final CharSequence README =
        "This is a marker file for a freeki content group. You can add pages to this group through the UI.";

    private final Logger logger = new Logger( getClass() );

    @Inject
    private FreekiConfig config;

    @Inject
    private JsonSerializer serializer;

    private Git git;

    private int basepathLength;

    @PostConstruct
    public void setupGit()
        throws IOException
    {
        basepathLength = config.getStorageDir()
                               .getPath()
                               .length() + 1;

        final File gitDir = new File( config.getStorageDir(), ".git" );
        final boolean create = !gitDir.isDirectory();

        final FileRepositoryBuilder builder = new FileRepositoryBuilder().setGitDir( gitDir )
                                                                         .readEnvironment();

        final FileRepository repo = builder.build();
        if ( create )
        {
            repo.create();
        }

        git = new Git( repo );
    }

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

    public void storeGroup( final Group group )
        throws IOException
    {
        final File groupDir = getFile( group.getName(), null );
        if ( !groupDir.exists() )
        {
            groupDir.mkdirs();
            final File readme = new File( groupDir, "README.txt" );
            FileUtils.write( readme, README );

            addAndCommit( readme, "Adding new group: " + group.getName() );
        }
    }

    private void addAndCommit( final File file, final String message )
        throws IOException
    {
        try
        {
            git.add()
               .addFilepattern( file.getPath() )
               .call();

            // TODO: Get the authorship info from somewhere...
            git.commit()
               .setAll( true )
               .setMessage( message )
               .setAuthor( "nobody", "user@nowhere.com" )
               .call();
        }
        catch ( final NoFilepatternException e )
        {
            throw new IOException( "Cannot add to git: " + e.getMessage(), e );
        }
        catch ( final GitAPIException e )
        {
            throw new IOException( "Cannot add to git: " + e.getMessage(), e );
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

        final boolean update = pageFile.exists();

        write( pageFile, page.render() );
        addAndCommit( pageFile, ( update ? "Updating" : "Creating" ) + " page: " + page.getTitle() + " in group: "
            + page.getGroup() );
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

        final Set<File> deleted = new HashSet<File>();
        if ( file.isDirectory() )
        {
            forceDelete( file );
            deleted.add( file );
            file = file.getParentFile();
        }
        else
        {
            file.delete();
            deleted.add( file );
            file = file.getParentFile();
        }

        while ( file.isDirectory() && !config.getStorageDir()
                                             .equals( file ) && file.list().length < 1 )
        {
            file.delete();
            deleted.add( file );
            file = file.getParentFile();
        }

        deleteAndCommit( deleted, "Removing page: " + title + " from group: " + group
            + ", and pruning empty directories." );
    }

    private void deleteAndCommit( final Set<File> deleted, final String message )
        throws IOException
    {
        try
        {
            final RmCommand rm = git.rm();
            for ( final File file : deleted )
            {
                rm.addFilepattern( file.getPath()
                                       .substring( basepathLength ) );
            }

            rm.call();

            // TODO: Get the authorship info from somewhere...
            git.commit()
               .setAll( true )
               .setMessage( message )
               .setAuthor( "nobody", "user@nowhere.com" )
               .call();
        }
        catch ( final NoFilepatternException e )
        {
            throw new IOException( "Cannot remove from git: " + e.getMessage(), e );
        }
        catch ( final GitAPIException e )
        {
            throw new IOException( "Cannot remove from git: " + e.getMessage(), e );
        }

    }

    public Set<Group> listGroups( final String subgroup )
    {
        final File root = config.getStorageDir();
        File dir = root;
        if ( subgroup != null )
        {
            dir = new File( dir, subgroup );
        }

        final String[] names = dir.isDirectory() ? dir.list() : new String[] {};
        final Set<Group> groups = new HashSet<Group>( names.length );
        for ( final String name : names )
        {
            final File f = new File( dir, name );
            if ( f.isDirectory() )
            {
                groups.add( new Group( subgroup, name ) );
            }
        }

        return groups;
    }

}
