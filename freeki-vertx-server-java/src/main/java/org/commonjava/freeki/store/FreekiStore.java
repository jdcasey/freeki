package org.commonjava.freeki.store;

import static org.apache.commons.io.FileUtils.forceDelete;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FileUtils.write;
import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.commonjava.freeki.conf.FreekiConfig;
import org.commonjava.freeki.model.ChildRef;
import org.commonjava.freeki.model.ChildRef.ChildType;
import org.commonjava.freeki.model.Group;
import org.commonjava.freeki.model.Page;
import org.commonjava.util.logging.Logger;
import org.eclipse.jgit.api.CommitCommand;
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

    private Git git;

    private int basepathLength;

    private String username;

    private String email;

    public FreekiStore()
    {
    }

    public FreekiStore( final FreekiConfig config )
        throws IOException
    {
        this.config = config;
        setupGit();
    }

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

        username = repo.getConfig()
                       .getString( "user", null, "name" );

        email = repo.getConfig()
                    .getString( "user", null, "email" );

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

    public SortedSet<ChildRef> listPages( final String group )
    {
        final SortedSet<ChildRef> refs = listChildren( group );
        for ( final Iterator<ChildRef> it = refs.iterator(); it.hasNext(); )
        {
            final ChildRef ref = it.next();
            if ( ChildType.GROUP == ref.getType() )
            {
                it.remove();
            }
        }

        return refs;
    }

    public SortedSet<ChildRef> listChildren( final String group )
    {
        final File root = config.getStorageDir();
        final File d = new File( root, group );

        final SortedSet<ChildRef> result = new TreeSet<ChildRef>();
        if ( d.isDirectory() )
        {
            logger.info( "Listing children in directory: %s\n", d );
            final File[] files = d.listFiles();

            for ( final File file : files )
            {
                final String name = file.getName();
                logger.info( name );

                if ( name.startsWith( "." ) )
                {
                    logger.info( "Skipping: %s\n", name );
                    continue;
                }

                if ( file.isDirectory() )
                {
                    result.add( new ChildRef( ChildType.GROUP, name, name ) );
                }
                else
                {
                    BufferedReader br = null;
                    try
                    {
                        br = new BufferedReader( new FileReader( file ) );
                        String title = Page.readTitle( br );
                        logger.info( "Page %s has title: %s\n", file, title );
                        if ( title == null )
                        {
                            title = file.getName();
                            if ( title.endsWith( ".md" ) )
                            {
                                title = title.substring( 0, title.length() - 3 );
                            }
                        }
                        result.add( new ChildRef( ChildType.PAGE, title, Page.idFor( title ) ) );
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
        }

        return result;
    }

    public boolean storeGroup( final Group group )
        throws IOException
    {
        final File groupDir = getFile( group.getName(), null );
        if ( !groupDir.exists() )
        {
            groupDir.mkdirs();
            final File readme = new File( groupDir, "README.txt" );
            FileUtils.write( readme, README );

            addAndCommit( readme, "Adding new group: " + group.getName() );
            return true;
        }

        return false;
    }

    public boolean storePage( final Page page )
        throws IOException
    {
        page.repair();

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

        return !update;
    }

    private File getFile( final String group, final String title )
    {
        return getFileById( group, title == null ? null : Page.idFor( title ) );
    }

    private File getFileById( final String group, final String id )
    {
        final File root = config.getStorageDir();
        final File groupDir = new File( root, group );
        return id == null ? groupDir : new File( groupDir, id + ".md" );
    }

    public Page getPage( final String group, final String id )
        throws IOException
    {
        System.out.printf( "Looking for page: %s in group: %s\n", id, group );
        final File file = getFileById( group, id );
        if ( !file.exists() )
        {
            System.out.printf( "Not a page: %s\n", file );
            return null;
        }

        System.out.printf( "Reading page from: %s\n", file );
        final String content = readFileToString( file );
        System.out.printf( "Page content:\n\n%s\n\n", content );

        return new Page( group, id, content, file.lastModified() );
    }

    public boolean deleteGroup( final String group )
        throws IOException
    {
        return doDelete( group, null );
    }

    public boolean deletePage( final String group, final String id )
        throws IOException
    {
        return doDelete( group, id );
    }

    private boolean doDelete( final String group, final String id )
        throws IOException
    {
        File file = getFileById( group, id );
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
        else if ( file.exists() )
        {
            file.delete();
            deleted.add( file );
            file = file.getParentFile();
        }

        // TODO: Will file.isDirectory() return true if it was just deleted, above??
        while ( file.isDirectory() && !config.getStorageDir()
                                             .equals( file ) && file.list().length < 1 )
        {
            file.delete();
            deleted.add( file );
            file = file.getParentFile();
        }

        if ( !deleted.isEmpty() )
        {
            deleteAndCommit( deleted, "Removing page: " + id + " from group: " + group
                + ", and pruning empty directories." );

            return true;
        }

        return false;
    }

    private void addAndCommit( final File file, final String message )
        throws IOException
    {
        try
        {
            final String filepath = file.getPath()
                                        .substring( basepathLength );

            git.add()
               .addFilepattern( filepath )
               .call();

            // TODO: Get the authorship info from somewhere...
            git.commit()
               .setOnly( filepath )
               .setMessage( message )
               .setAuthor( username, email )
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

    private void deleteAndCommit( final Set<File> deleted, final String message )
        throws IOException
    {
        try
        {
            RmCommand rm = git.rm();
            CommitCommand commit = git.commit();

            for ( final File file : deleted )
            {
                final String filepath = file.getPath()
                                            .substring( basepathLength );

                rm = rm.addFilepattern( filepath );
                commit = commit.setOnly( filepath );
            }

            rm.call();

            // TODO: Get the authorship info from somewhere...
            commit.setMessage( message )
                  .setAuthor( username, email )
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

    public SortedSet<Group> listGroups( final String subgroup )
        throws IOException
    {
        final File root = config.getStorageDir();
        File dir = root;
        if ( subgroup != null )
        {
            dir = new File( dir, subgroup );
        }

        final String[] names = dir.isDirectory() ? dir.list() : new String[] {};
        final SortedSet<Group> groups = new TreeSet<Group>();
        for ( final String name : names )
        {
            final File f = new File( dir, name );
            if ( f.isDirectory() )
            {
                final String groupName = Group.nameFor( subgroup, name );
                final SortedSet<ChildRef> pages = listChildren( groupName );
                groups.add( new Group( groupName, pages ) );
            }
        }

        return groups;
    }

    public Group getGroup( final String groupName )
        throws IOException
    {
        if ( groupName == null || groupName.trim()
                                           .length() < 1 )
        {
            return null;
        }

        final File groupDir = new File( config.getStorageDir(), groupName );

        if ( !groupDir.isDirectory() )
        {
            return null;
        }

        final SortedSet<ChildRef> pages = listChildren( groupName );
        return new Group( groupName, pages );
    }
}
