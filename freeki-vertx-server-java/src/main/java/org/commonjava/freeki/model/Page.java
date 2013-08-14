package org.commonjava.freeki.model;

import static org.commonjava.freeki.model.MetadataKeys.CREATED;
import static org.commonjava.freeki.model.MetadataKeys.CURRENT_AUTHOR;
import static org.commonjava.freeki.model.MetadataKeys.TITLE;
import static org.commonjava.freeki.model.MetadataKeys.metadataKey;
import static org.commonjava.freeki.model.io.DateSerializer.formatDate;
import static org.commonjava.freeki.model.io.DateSerializer.parseDate;
import static org.commonjava.freeki.util.UrlUtils.buildUrl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.LineIterator;
import org.commonjava.freeki.infra.anno.RenderKey;
import org.commonjava.util.logging.Logger;

@RenderKey( key = "page", contentTemplate = "${data.content}" )
public class Page
{

    private static final String COMMENT_START = "<!---";

    private static final String COMMENT_END = "-->";

    private static final char LS = '\n';

    private static final String KVS = ": ";

    private transient final Logger logger = new Logger( getClass() );

    private String id;

    private String serverPath;

    private String title;

    private Date created = new Date();

    private Date updated = new Date();

    private String currentAuthor;

    private String content;

    private String group;

    private final Map<String, String> metadata = new HashMap<>();

    public Page()
    {
    }

    public Page( final String group, final String id, final String content, final long lastUpdated )
        throws MalformedURLException
    {
        this.group = group;
        this.updated = new Date( lastUpdated );

        System.out.printf( "Parsing page metadata for %s...", id );
        this.content = parse( content );
        System.out.printf( "...done. Content:\n\n'%s'\n\n", this.content );
    }

    public void repair()
        throws MalformedURLException
    {
        this.id = idFor( title );
        this.serverPath = serverPathFor( group, title );
    }

    public String getContent()
    {
        return content;
    }

    public Page setContent( final String content )
    {
        this.content = content;
        return this;
    }

    public void syncContentWithTitle()
    {
        String title = getTitle();
        if ( content != null )
        {
            boolean prependTitle = false;

            final LineIterator li = new LineIterator( new StringReader( content ) );
            while ( li.hasNext() )
            {
                final String line = li.next();
                if ( line.trim()
                         .length() > 0 )
                {
                    if ( title == null )
                    {
                        title = line;
                    }
                    else if ( title != null )
                    {
                        if ( !line.trim()
                                  .endsWith( title.trim() ) )
                        {
                            prependTitle = true;
                        }
                    }

                    break;
                }
            }

            if ( prependTitle )
            {
                content = "#" + title + "\n\n" + content;
            }
        }
        else if ( title != null )
        {
            content = "#" + title + "\n\n";
        }

        setTitle( title );
    }

    public String getTitle()
    {
        return title;
    }

    public Date getCreated()
    {
        return created;
    }

    public Date getUpdated()
    {
        return updated;
    }

    public String getCurrentAuthor()
    {
        return currentAuthor;
    }

    public Page setTitle( final String title )
    {
        this.title = title;
        return this;
    }

    public Page setCreated( final Date created )
    {
        this.created = created;
        return this;
    }

    public Page setUpdated( final Date updated )
    {
        this.updated = updated;
        return this;
    }

    public Page setCurrentAuthor( final String currentAuthor )
    {
        this.currentAuthor = currentAuthor;
        return this;
    }

    public Page setGroup( final String group )
    {
        this.group = group;
        return this;
    }

    public String getGroup()
    {
        return group;
    }

    public CharSequence render()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append( COMMENT_START )
          .append( LS )
          .append( MetadataKeys.TITLE )
          .append( KVS )
          .append( title )
          .append( LS )
          .append( LS )
          .append( CREATED )
          .append( KVS )
          .append( formatDate( created ) )
          .append( LS )
          .append( CURRENT_AUTHOR )
          .append( KVS )
          .append( currentAuthor )
          .append( LS )
          .append( COMMENT_END )
          .append( LS )
          .append( LS );

        if ( !content.startsWith( "#" + title ) )
        {
            sb.append( '#' )
              .append( title )
              .append( LS )
              .append( LS );
        }

        sb.append( content );

        return sb;
    }

    private String parse( final String content )
        throws MalformedURLException
    {
        final String[] lines = content.split( Pattern.quote( "\n" ) );

        boolean commentStarted = false;
        int headerSize = 0;

        for ( final String line : lines )
        {
            headerSize++;

            if ( line.trim()
                     .startsWith( COMMENT_START ) )
            {
                commentStarted = true;
            }
            else if ( line.trim()
                          .endsWith( COMMENT_END ) )
            {
                break;
            }
            else if ( commentStarted )
            {
                final int idx = line.indexOf( ':' );
                if ( idx > 0 )
                {
                    final String key = line.substring( 0, idx )
                                           .trim()
                                           .toLowerCase();

                    final String value = line.substring( idx + 1 )
                                             .trim();

                    final MetadataKeys mk = metadataKey( key );
                    if ( mk == null )
                    {
                        metadata.put( key, value );
                        continue;
                    }

                    switch ( mk )
                    {
                        case TITLE:
                        {
                            this.title = value;
                            break;
                        }
                        case CREATED:
                        {
                            try
                            {
                                this.created = parseDate( value );
                            }
                            catch ( final ParseException e )
                            {
                                logger.error( "Failed to parse creation date '%s' on page: '%s'", value,
                                              title == null ? "Unknown page" : title );
                            }

                            break;
                        }
                        case CURRENT_AUTHOR:
                        {
                            this.currentAuthor = value;
                            break;
                        }
                        default:
                        {
                        }
                    }
                }
            }
        }

        this.id = serverPathFor( group, title );

        boolean contentStarted = false;
        final StringBuilder sb = new StringBuilder();
        for ( int i = headerSize + 1; i < lines.length; i++ )
        {
            if ( !contentStarted )
            {
                if ( lines[i].isEmpty() )
                {
                    continue;
                }
                else
                {
                    contentStarted = true;
                }
            }

            if ( sb.length() > 0 )
            {
                sb.append( "\n" );
            }

            sb.append( lines[i] );
        }

        return sb.toString();
    }

    public static String readTitle( final BufferedReader reader )
        throws IOException
    {
        System.out.println( "Looking for title in markdown header." );
        if ( reader == null )
        {
            return null;
        }

        String line = null;
        boolean headerStarted = false;

        String title = null;
        while ( ( line = reader.readLine() ) != null )
        {
            if ( line.trim()
                     .startsWith( COMMENT_START ) )
            {
                System.out.println( "In header." );
                headerStarted = true;
            }
            else if ( line.trim()
                          .endsWith( COMMENT_END ) )
            {
                System.out.println( "Out of header." );
                break;
            }
            else if ( headerStarted )
            {
                final int idx = line.indexOf( ':' );
                if ( idx > 0 )
                {
                    final String key = line.substring( 0, idx )
                                           .trim()
                                           .toLowerCase();

                    final String value = line.substring( idx + 1 )
                                             .trim();

                    System.out.printf( "HEADER\n  Key: %s\n  Value: %s\n\n", key, value );
                    if ( TITLE.name()
                              .equalsIgnoreCase( key ) )
                    {
                        title = value;
                        System.out.printf( "Got title: %s\n", title );
                        break;
                    }
                }
            }
        }

        return title;
    }

    public boolean updateFrom( final Page pg )
    {
        if ( pg.group != null && !group.equals( pg.group ) )
        {
            return false;
        }

        if ( pg.title != null && !title.equals( pg.title ) )
        {
            return false;
        }

        this.currentAuthor = pg.currentAuthor;
        this.updated = pg.updated;
        this.content = pg.content;

        return true;
    }

    public static String serverPathFor( final String group, final String title )
        throws MalformedURLException
    {
        return buildUrl( false, group, idFor( title ) );
    }

    public static String idFor( final String title )
    {
        return title.toLowerCase()
                    .replaceAll( "[^-_a-zA-Z0-9]+", "-" );
    }

    public String getId()
    {
        return id;
    }

    public void setId( final String id )
    {
        this.id = id;
    }

    public String getServerPath()
    {
        return serverPath;
    }

    public void setServerPath( final String serverPath )
    {
        this.serverPath = serverPath;
    }

    @Override
    public String toString()
    {
        return String.format( "Page [id=%s, title=%s, group=%s]", id, title, group );
    }

}
