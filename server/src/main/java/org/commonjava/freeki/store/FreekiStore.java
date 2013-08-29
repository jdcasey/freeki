package org.commonjava.freeki.store;

import static org.apache.commons.io.FileUtils.forceDelete;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FileUtils.write;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
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

public class FreekiStore
{

    private static final CharSequence README = "This is a marker file for a freeki content group. You can add pages to this group through the UI.";

    private static final String COMMENT_BEGIN = "<!--";

    private static final String COMMENT_END = "-->";

    private static final String MD_TITLE = "TITLE";

    private static final String METADATA_WARNING = "Freeki metadata. Do not remove this section!";

    private static final String H1 = "# ";

    private static final String H1_ALT = "#";

    private static final String MD_CONTENT = "CONTENT";

    private final Logger logger = new Logger( getClass() );

    private final FreekiConfig config;

    private Git git;

    private int basepathLength;

    private String username;

    private String email;

    private FileRepository repo;

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
        final File file = getFileByTitle( group, title );
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
                        final String content = FileUtils.readFileToString( file );
                        final Map<String, String> metadata = getMetadata( content, false );
                        final String title = metadata.get( MD_TITLE );

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

    public boolean storeGroup( final Group group )
        throws IOException
    {
        final File groupDir = getFile( group.getName() );
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
        final File pageFile = getFile( page.getId() + ".md" );
        final File dir = pageFile.getParentFile();

        if ( !dir.isDirectory() && !dir.mkdirs() )
        {
            throw new IOException( "Failed to create directory structure for page: " + dir.getAbsolutePath() );
        }

        final boolean update = pageFile.exists();

        if ( page.getTitle() == null )
        {
            final Map<String, String> existingMeta = page.getMetadata();
            final Map<String, String> parsed = getMetadata( page.getContent(), false );
            parsed.putAll( existingMeta );

            page.setMetadata( parsed );
            page.setTitle( parsed.get( MD_TITLE ) );
        }

        String content = page.getContent();

        final Map<String, String> meta = page.getMetadata();
        if ( meta.get( MD_TITLE ) == null )
        {
            meta.put( MD_TITLE, page.getTitle() );
        }

        if ( meta != null && !meta.isEmpty() )
        {
            final StringBuilder sb = new StringBuilder();
            sb.append( COMMENT_BEGIN )
              .append( " " )
              .append( METADATA_WARNING );

            for ( final Entry<String, String> entry : meta.entrySet() )
            {
                final String key = entry.getKey();
                final String value = entry.getValue();

                sb.append( '\n' )
                  .append( key )
                  .append( ": " )
                  .append( value );
            }

            sb.append( '\n' )
              .append( COMMENT_END );
            sb.append( '\n' )
              .append( content );

            content = sb.toString();
        }

        write( pageFile, content );

        addAndCommit( pageFile, ( update ? "Updating" : "Creating" ) );

        return !update;
    }

    private File getFile( final String id )
    {
        return getFileById( id, null );
    }

    private File getFileByTitle( final String pathBase, final String title )
    {
        return getFileById( pathBase, title == null ? null : Page.idFor( title ) );
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
        String content = readFileToString( file );
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

        final Map<String, String> metadata = getMetadata( content, true );
        content = metadata.remove( MD_CONTENT );
        final String title = metadata.get( MD_TITLE );
        final PersonIdent ai = commit.getAuthorIdent();

        return new Page( group, id, content, title, ai.getWhen()
                                                      .getTime(), ai.getName() );
    }

    private Map<String, String> getMetadata( final String content, final boolean reformatContent )
    {
        final LineIterator li = new LineIterator( new StringReader( content ) );
        final Map<String, String> metadata = new HashMap<>();

        final StringBuilder sb = new StringBuilder();

        boolean startMetadata = false;
        boolean stopMetadata = false;
        boolean startContent = false;
        while ( li.hasNext() )
        {
            final String line = li.next();
            if ( line.trim()
                     .startsWith( COMMENT_BEGIN ) )
            {
                startMetadata = true;
            }
            else if ( line.trim()
                          .endsWith( COMMENT_END ) )
            {
                stopMetadata = true;
            }
            else if ( startMetadata && !stopMetadata )
            {
                final String[] parts = line.trim()
                                           .split( "\\s*:\\s*" );
                if ( parts.length == 2 )
                {
                    metadata.put( parts[0], parts[1] );
                }
            }
            else if ( stopMetadata && !startContent && line.trim()
                                                           .length() > 0 )
            {
                startContent = true;
            }

            if ( startContent )
            {
                String title = metadata.get( MD_TITLE );
                if ( title == null && line.startsWith( H1 ) )
                {
                    title = line.substring( H1.length() );
                    metadata.put( MD_TITLE, title );
                }
                else if ( title == null && line.startsWith( H1_ALT ) )
                {
                    title = line.substring( H1_ALT.length() );
                    if ( !title.startsWith( "#" ) )
                    {
                        metadata.put( MD_TITLE, title );
                    }
                }

                if ( !reformatContent && title != null )
                {
                    break;
                }

                if ( sb.length() > 0 )
                {
                    sb.append( '\n' );
                }

                sb.append( line );
            }
        }

        metadata.put( MD_CONTENT, sb.toString() );

        return metadata;
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
