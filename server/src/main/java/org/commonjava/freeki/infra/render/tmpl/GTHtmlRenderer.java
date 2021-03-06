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

import static org.commonjava.freeki.infra.render.RenderUtils.getContentTemplate;
import static org.commonjava.freeki.infra.render.RenderUtils.getTemplateKey;
import static org.commonjava.freeki.util.ContentType.TEXT_HTML;
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
import org.markdown4j.Markdown4jProcessor;

public class GTHtmlRenderer
    implements ContentRenderer
{

    private static final ContentType[] TYPES = { TEXT_HTML };

    private final GStringTemplateEngine engine;

    //    private final PegDownProcessor proc;

    private final GTemplateConfig config;

    public GTHtmlRenderer( final GStringTemplateEngine engine, /*final PegDownProcessor proc,*/final GTemplateConfig config )
    {
        this.engine = engine;
        //        this.proc = proc;
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
        final String path = config.getTemplate( TEXT_HTML, getTemplateKey( data ) );
        //        System.out.printf( "Using template: %s\n", path );
        try
        {
            final Map<String, Object> map = new HashMap<String, Object>();
            map.put( "data", data );
            map.put( "readOnly", config.isReadOnly() );
            map.put( "contentOnly", requestParams != null && "content-only".equals( requestParams.get( "format" ) ) );
            map.put( "params", requestParams );

            final String ct = getContentTemplate( data );
            String content;
            if ( ct == null )
            {
                content = null;
            }
            else
            {
                final Template contentTemplate = engine.createTemplate( ct );
                final Writable out = contentTemplate.make( map );

                final StringWriter writer = new StringWriter();
                out.writeTo( writer );

                content = writer.toString();
            }

            if ( content != null )
            {
                System.out.printf( "Extracted raw content:\n\n%s\n\n", content );
                final String renderedMarkdown = new Markdown4jProcessor().process( content );
                map.put( "rendered", renderedMarkdown );
            }

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

            final Writable output = template.make( map );

            final StringWriter writer = new StringWriter();
            output.writeTo( writer );

            final String html = writer.toString();
            //            System.out.printf( "Rendered html:\n\n%s\n\n", html );

            return html;
        }
        catch ( CompilationFailedException | ClassNotFoundException | IOException e )
        {
            throw new RenderingException( "Failed to load template: %s. Reason: %s", e, path, e.getMessage() );
        }
    }

}
