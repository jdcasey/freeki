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
package org.commonjava.freeki.infra.render.tmpl;

import static org.commonjava.freeki.infra.render.RenderUtils.getTemplateKey;
import static org.commonjava.freeki.util.ContentType.TEXT_PLAIN;
import groovy.lang.Writable;
import groovy.text.GStringTemplateEngine;
import groovy.text.Template;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.groovy.control.CompilationFailedException;
import org.commonjava.freeki.conf.GTemplateConfig;
import org.commonjava.freeki.infra.render.ContentRenderer;
import org.commonjava.freeki.infra.render.RenderingException;
import org.commonjava.freeki.util.ContentType;

public class GTTextRenderer
    implements ContentRenderer
{

    private static final ContentType[] TYPES = { TEXT_PLAIN };

    private final GStringTemplateEngine engine;

    private final GTemplateConfig config;

    public GTTextRenderer( final GStringTemplateEngine engine, final GTemplateConfig config )
    {
        this.engine = engine;
        this.config = config;
    }

    @Override
    public ContentType[] getContentTypes()
    {
        return TYPES;
    }

    @Override
    public String render( final Object data, final Map<String, String> requestParams )
        throws RenderingException
    {
        final String path = config.getTemplate( TEXT_PLAIN, getTemplateKey( data ) );
        try
        {
            final Template template;
            final File templateFile = new File( config.getBrandingDir(), path );
            if ( templateFile.exists() && !templateFile.isDirectory() )
            {
                template = engine.createTemplate( templateFile );
            }
            else
            {
                final URL u = Thread.currentThread()
                                    .getContextClassLoader()
                                    .getResource( path );
                template = u == null ? null : engine.createTemplate( u );
            }

            if ( template == null )
            {
                throw new RenderingException( "Failed to locate template: %s", path );
            }

            final Map<String, Object> map = new HashMap<String, Object>();
            map.put( "data", data );
            map.put( "readOnly", config.isReadOnly() );
            map.put( "params", requestParams );

            final Writable output = template.make( map );

            final StringWriter writer = new StringWriter();
            output.writeTo( writer );

            return writer.toString();
        }
        catch ( CompilationFailedException | ClassNotFoundException | IOException e )
        {
            throw new RenderingException( "Failed to load template: %s. Reason: %s", e, path, e.getMessage() );
        }
    }

}
