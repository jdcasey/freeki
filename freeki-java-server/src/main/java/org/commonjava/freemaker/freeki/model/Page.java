package org.commonjava.freemaker.freeki.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.io.LineIterator;
import org.commonjava.freemaker.freeki.model.io.PageJsonAdapter;
import org.commonjava.util.logging.Logger;
import org.commonjava.web.json.ser.JsonAdapters;

@JsonAdapters( PageJsonAdapter.class )
public class Page
{

    public static final String CREATED = "Created: ";

    public static final String CURRENT_AUTHOR = "Current-Author: ";

    private static final String TITLE = "Title: ";

    private static final String COMMENT_START = "<!---";

    private static final String COMMENT_END = "--->";

    private static final char LS = '\n';

    private static final String KVS = ": ";

    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm Z";

    private final Logger logger = new Logger( getClass() );

    private String title;

    private Date created;

    private Date updated;

    private String currentAuthor;

    private String content;

    private String group;

    public Page()
    {
    }

    private Page( final String group, final String title )
    {
        this.group = group;
        this.title = title;
    }

    public Page( final String content )
    {
        this.content = content;
        parse();
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
          .append( TITLE )
          .append( KVS )
          .append( title )
          .append( LS )
          .append( CREATED )
          .append( KVS )
          .append( new SimpleDateFormat( DATE_FORMAT ).format( created ) )
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

    public static Page lookupInstance( final String group, final String title )
    {
        return new Page( group, title );
    }

    private void parse()
    {
        final String[] lines = content.split( Pattern.quote( "\n" ) );

        boolean commentStarted = false;
        int headerSize = 0;
        for ( final String line : lines )
        {
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
                final String[] parts = line.split( "\\s*:\\s*" );
                if ( parts.length == 2 )
                {
                    final String key = parts[0].trim()
                                               .toLowerCase();
                    final String value = parts[1].trim();

                    if ( TITLE.equals( key ) )
                    {
                        this.title = value;
                    }
                    else if ( CREATED.equals( key ) )
                    {
                        try
                        {
                            this.created = new SimpleDateFormat( DATE_FORMAT ).parse( value );
                        }
                        catch ( final ParseException e )
                        {
                            logger.error( "Failed to parse creation date '%s' on page: '%s'", value,
                                          title == null ? "Unknown page" : title );
                        }
                    }
                    else if ( CURRENT_AUTHOR.equals( key ) )
                    {
                        this.currentAuthor = value;
                    }
                }
            }

            headerSize++;
        }

        final StringBuilder sb = new StringBuilder();
        for ( int i = headerSize; i < lines.length; i++ )
        {
            if ( sb.length() > 0 )
            {
                sb.append( "\n" );
            }

            sb.append( lines[i] );
        }

        this.content = sb.toString();
    }

    public static String readTitle( final BufferedReader reader )
        throws IOException
    {
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
                headerStarted = true;
            }
            else if ( line.trim()
                          .startsWith( COMMENT_END ) )
            {
                break;
            }
            else if ( headerStarted )
            {
                final String[] parts = line.split( "\\s*:\\s*" );
                if ( parts.length == 2 && TITLE.equals( parts[0].trim()
                                                                .toLowerCase() ) )
                {
                    title = parts[1].trim();
                    break;
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

}