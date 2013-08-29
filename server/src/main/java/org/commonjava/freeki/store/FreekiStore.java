package org.commonjava.freeki.store;

import static org.apache.commons.io.FileUtils.forceDelete;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FileUtils.write;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.commonjava.freeki.conf.FreekiConfig;
import org.commonjava.freeki.model.ChildRef;
import org.commonjava.freeki.model.ChildRef.ChildType;
import org.commonjava.freeki.model.Group;
import org.commonjava.freeki.model.Page;
import org.commonjava.util.logging.Logger;
import org.commonjava.web.json.ser.JsonSerializer;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RmCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revplot.PlotCommitList;
import org.eclipse.jgit.revplot.PlotLane;
import org.eclipse.jgit.revplot.PlotWalk;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.filter.AndTreeFilter;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;

import com.google.gson.reflect.TypeToken;

public class FreekiStore
{

    private static final CharSequence README = "This is a marker file for a freeki content group. You can add pages to this group through the UI.";

    private static final String PAGE_TITLE = "title";

    private static final String PAGE_STATUS = "status";

    private final Logger logger = new Logger( getClass() );

    private final FreekiConfig config;

    private final JsonSerializer serializer;

    private Git git;

    private int basepathLength;

    private String username;

    private String email;

    private FileRepository repo;

    public FreekiStore( final FreekiConfig config, final JsonSerializer serializer )
        throws IOException
    {
        this.config = config;
        this.serializer = serializer;
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

        repo = builder.build();

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
                    try
                    {
                        final String fname = file.getName();
                        final PlotCommit<PlotLane> commit = getHeadCommit( file );
                        final String title = getTitle( commit );

                        result.add( new ChildRef( ChildType.PAGE, title, fname.substring( 0, fname.length() - 3 ) ) );
                    }
                    catch ( final Exception e )
                    {
                        logger.error( "Cannot read: '%s'. Reason: %s", e, file, e.getMessage() );
                    }
                }
            }
        }

        return result;
    }

    private String getTitle( final PlotCommit<PlotLane> commit )
    {
        final Map<String, String> map = serializer.fromString( commit.getFullMessage(), new TypeToken<Map<String, String>>()
        {
        } );
        return map.get( PAGE_TITLE );
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

        write( pageFile, page.getContent() );

        final Map<String, String> meta = new HashMap<>();
        meta.put( PAGE_TITLE, page.getTitle() );
        meta.put( PAGE_STATUS, ( update ? "Updating" : "Creating" ) );
        addAndCommit( pageFile, serializer.toString( meta ) );

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

        PlotCommit<PlotLane> commit;
        try
        {
            commit = getHeadCommit( file );
        }
        catch ( final Exception e )
        {
            throw new IOException( String.format( "Failed to read commit information for: %s. Reason: %s", file, e.getMessage() ), e );
        }

        final String title = getTitle( commit );
        final PersonIdent ai = commit.getAuthorIdent();

        return new Page( group, id, content, title, ai.getWhen()
                                                      .getTime(), ai.getName() );
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
            deleteAndCommit( deleted, "Removing page: " + id + " from group: " + group + ", and pruning empty directories." );

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

    // FIXME: Refine throws.
    public PlotCommit<PlotLane> getHeadCommit( final File f )
        throws Exception
    {
        final ObjectId oid = repo.resolve( "HEAD" );
        final PlotWalk pw = new PlotWalk( repo );
        final RevCommit rc = pw.parseCommit( oid );
        pw.markStart( rc );
        pw.setTreeFilter( AndTreeFilter.create( PathFilter.create( f.getPath()
                                                                    .substring( basepathLength ) ), TreeFilter.ANY_DIFF ) );

        final PlotCommitList<PlotLane> cl = new PlotCommitList<>();
        cl.source( pw );
        cl.fillTo( 1 );

        return cl.get( 0 );
        //        final PersonIdent ident = pc.getAuthorIdent();
        //        final String message = pc.getFullMessage();
        //        System.out.printf( "%s %s %s %s %s\n\n%s\n", ident.getName(), ident.getEmailAddress(), ident.getWhen(), ident.getTimeZone()
        //                                                                                                                     .getID(),
        //                           ident.getTimeZoneOffset(), message );
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
