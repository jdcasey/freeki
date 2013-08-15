package org.commonjava.freeki.conf;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.context.ApplicationScoped;

import org.commonjava.freeki.util.ContentType;
import org.commonjava.web.config.ConfigurationException;
import org.commonjava.web.config.annotation.SectionName;
import org.commonjava.web.config.section.MapSectionListener;

@ApplicationScoped
@SectionName( "templates" )
public class GTemplateConfig
    extends MapSectionListener
{

    private Map<ContentKey, URL> templates;

    public GTemplateConfig()
    {
    }

    public GTemplateConfig( final Map<String, String> rawTemplateMap )
    {
        parseRawTemplateMap( rawTemplateMap );
    }

    public URL getTemplate( final ContentType type, final String renderKey )
    {
        return templates.get( new ContentKey( type, renderKey ) );
    }

    private static final class ContentKey
    {
        private final ContentType type;

        private final String renderKey;

        private ContentKey( final ContentType type, final String renderKey )
        {
            this.type = type;
            this.renderKey = renderKey;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ( ( renderKey == null ) ? 0 : renderKey.hashCode() );
            result = prime * result + ( ( type == null ) ? 0 : type.hashCode() );
            return result;
        }

        @Override
        public boolean equals( final Object obj )
        {
            if ( this == obj )
            {
                return true;
            }
            if ( obj == null )
            {
                return false;
            }
            if ( getClass() != obj.getClass() )
            {
                return false;
            }
            final ContentKey other = (ContentKey) obj;
            if ( renderKey == null )
            {
                if ( other.renderKey != null )
                {
                    return false;
                }
            }
            else if ( !renderKey.equals( other.renderKey ) )
            {
                return false;
            }
            if ( type != other.type )
            {
                return false;
            }
            return true;
        }

    }

    @Override
    public void sectionComplete( final String name )
        throws ConfigurationException
    {
        super.sectionComplete( name );

        final Map<String, String> configuration = super.getConfiguration();

        parseRawTemplateMap( configuration );
    }

    private void parseRawTemplateMap( final Map<String, String> rawTemplateMap )
    {
        final Map<ContentKey, URL> templates = new HashMap<ContentKey, URL>();
        for ( final Entry<String, String> entry : rawTemplateMap.entrySet() )
        {
            final String key = entry.getKey();
            final String value = entry.getValue();

            System.out.printf( "Parsing raw template-map entry: %s = %s\n", key, value );

            if ( value == null )
            {
                continue;
            }

            final String[] keyParts = key.split( "@" );
            if ( keyParts.length < 2 )
            {
                continue;
            }

            final ContentType type = ContentType.find( keyParts[1] );
            System.out.printf( "Got content type: %s\nGot render key: %s\n", type, keyParts[1] );
            if ( type == null )
            {
                continue;
            }

            final ContentKey ck = new ContentKey( type, keyParts[0] );
            final URL url = Thread.currentThread()
                                  .getContextClassLoader()
                                  .getResource( value );
            if ( url == null )
            {
                continue;
            }

            templates.put( ck, url );
        }

        this.templates = templates;
    }

}