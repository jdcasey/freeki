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
package org.commonjava.freeki.data;

import static org.apache.commons.io.FileUtils.forceDelete;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FileUtils.write;

import java.io.File;
import java.io.FilenameFilter;
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.commonjava.freeki.conf.FreekiConfig;
import org.commonjava.freeki.model.ChildRef;
import org.commonjava.freeki.model.ChildRef.ChildType;
import org.commonjava.freeki.model.Group;
import org.commonjava.freeki.model.Page;
import org.commonjava.util.logging.Logger;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revplot.PlotLane;

public class FreekiStore
{

    public class ValidChildrenFilenameFilter
        implements FilenameFilter
    {
        @Override
        public boolean accept( final File dir, final String name )
        {
            return name.endsWith( ".md" ) || new File( dir, name ).isDirectory();
        }
    }

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

    private final GitManager git;

    public FreekiStore( final FreekiConfig config, final GitManager git )
        throws IOException
    {
        this.config = config;
        this.git = git;
    }

    public boolean hasGroup( final String group )
    {
        final File root = config.getContentDir();
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
        final File root = config.getContentDir();
        final File d = new File( root, group );

        final SortedSet<ChildRef> result = new TreeSet<ChildRef>();
        if ( d.isDirectory() )
        {
            //            logger.info( "Listing children in directory: %s\n", d );
            final File[] files = d.listFiles( new ValidChildrenFilenameFilter() );

            for ( final File file : files )
            {
                final String name = file.getName();
                //                logger.info( name );

                if ( name.startsWith( "." ) )
                {
                    //                    logger.info( "Skipping: %s\n", name );
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
                        String fname = file.getName();
                        fname = fname.substring( 0, fname.length() - 3 );

                        final String content = FileUtils.readFileToString( file );
                        final Map<String, String> metadata = getMetadata( content, false );
                        String title = metadata.get( MD_TITLE );
                        if ( title == null )
                        {
                            title = fname;
                        }

                        result.add( new ChildRef( ChildType.PAGE, title, fname ) );
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

            git.addAndCommit( "Adding new group: " + group.getName(), readme );
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

        git.addAndCommit( ( update ? "Updating: " : "Creating: " ) + page.getTitle(), pageFile );

        return !update;
    }

    private File getFile( final String id )
    {
        return getFileById( id, null );
    }

    private File getFileByTitle( final String group, final String title )
    {
        return getFileById( title == null ? group : Page.serverPathFor( group, title ), null );
    }

    private File getFileById( final String group, final String id )
    {
        final File root = config.getContentDir();
        //        logger.info( "Calculating file from base: %s, group: %s, page-id: %s", root, group, id );
        final File groupDir = group.length() < 1 || group.equals( "/" ) ? root : new File( root, group );
        return id == null ? groupDir : new File( groupDir, id + ".md" );
    }

    public Page getPage( final String group, final String id )
        throws IOException
    {
        final File file = getFileById( group, id );
        if ( !file.exists() )
        {
            return null;
        }

        String content = readFileToString( file );

        PlotCommit<PlotLane> commit;
        try
        {
            commit = git.getHeadCommit( file );
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
            file = new File( config.getContentDir(), group );
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
        while ( file.isDirectory() && !config.getContentDir()
                                             .equals( file ) && file.list().length < 1 )
        {
            file.delete();
            deleted.add( file );
            file = file.getParentFile();
        }

        if ( !deleted.isEmpty() )
        {
            git.deleteAndCommit( "Removing page: " + id + " from group: " + group + ", and pruning empty directories.", deleted );

            return true;
        }

        return false;
    }

    public SortedSet<Group> listGroups( final String subgroup )
        throws IOException
    {
        final File root = config.getContentDir();
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

        final File groupDir = new File( config.getContentDir(), groupName );

        if ( !groupDir.isDirectory() )
        {
            return null;
        }

        final SortedSet<ChildRef> pages = listChildren( groupName );
        return new Group( groupName, pages );
    }

}
