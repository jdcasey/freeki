package org.commonjava.freeki.infra.render.tmpl;

import static org.commonjava.freeki.infra.render.RenderUtils.getContentTemplate;
import static org.commonjava.freeki.infra.render.RenderUtils.getTemplateKey;
import static org.commonjava.freeki.util.ContentType.TEXT_HTML;
import groovy.lang.Writable;
import groovy.text.GStringTemplateEngine;
import groovy.text.Template;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.codehaus.groovy.control.CompilationFailedException;
import org.commonjava.freeki.conf.GTemplateConfig;
import org.commonjava.freeki.infra.render.ContentRenderer;
import org.commonjava.freeki.infra.render.RenderingException;
import org.commonjava.freeki.util.ContentType;
import org.pegdown.PegDownProcessor;

public class GTHtmlRenderer
    implements ContentRenderer
{

    private static final ContentType[] TYPES = { TEXT_HTML };

    @Inject
    private GStringTemplateEngine engine;

    @Inject
    private PegDownProcessor proc;

    @Inject
    private GTemplateConfig config;

    public GTHtmlRenderer()
    {
    }

    public GTHtmlRenderer( final GStringTemplateEngine engine, final PegDownProcessor proc, final GTemplateConfig config )
    {
        this.engine = engine;
        this.proc = proc;
        this.config = config;
    }

    @Override
    public ContentType[] getContentTypes()
    {
        return TYPES;
    }

    @Override
    public String render( final Object data )
        throws RenderingException
    {
        final URL url = config.getTemplate( TEXT_HTML, getTemplateKey( data ) );
        System.out.printf( "Using template: %s\n", url );
        try
        {
            final Map<String, Object> map = new HashMap<String, Object>();
            map.put( "data", data );

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
                final String renderedMarkdown = proc.markdownToHtml( content );
                map.put( "rendered", renderedMarkdown );
            }

            final Template template = engine.createTemplate( url );
            final Writable output = template.make( map );

            final StringWriter writer = new StringWriter();
            output.writeTo( writer );

            final String html = writer.toString();
            System.out.printf( "Rendered html:\n\n%s\n\n", html );

            return html;
        }
        catch ( CompilationFailedException | ClassNotFoundException | IOException e )
        {
            throw new RenderingException( "Failed to load template: %s. Reason: %s", e, url, e.getMessage() );
        }
    }

}
