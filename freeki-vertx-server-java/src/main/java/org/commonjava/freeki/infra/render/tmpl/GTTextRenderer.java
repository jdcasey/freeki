package org.commonjava.freeki.infra.render.tmpl;

import static org.commonjava.freeki.infra.render.RenderUtils.getTemplateKey;
import static org.commonjava.freeki.util.ContentType.TEXT_PLAIN;
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

public class GTTextRenderer
    implements ContentRenderer
{

    private static final ContentType[] TYPES = { TEXT_PLAIN };

    @Inject
    private GStringTemplateEngine engine;

    @Inject
    private GTemplateConfig config;

    public GTTextRenderer()
    {
    }

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
    public String render( final Object data )
        throws RenderingException
    {
        final URL url = config.getTemplate( TEXT_PLAIN, getTemplateKey( data ) );
        try
        {
            final Template template = engine.createTemplate( url );

            final Map<String, Object> map = new HashMap<String, Object>();
            map.put( "data", data );

            final Writable output = template.make( map );

            final StringWriter writer = new StringWriter();
            output.writeTo( writer );

            return writer.toString();
        }
        catch ( CompilationFailedException | ClassNotFoundException | IOException e )
        {
            throw new RenderingException( "Failed to load template: %s. Reason: %s", e, url, e.getMessage() );
        }
    }

}
