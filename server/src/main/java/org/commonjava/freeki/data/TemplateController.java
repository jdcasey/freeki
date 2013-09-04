package org.commonjava.freeki.data;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.control.CompilationFailedException;
import org.commonjava.freeki.conf.FreekiConfig;
import org.commonjava.freeki.model.Template;
import org.commonjava.freeki.model.TemplateAction;
import org.commonjava.util.logging.Logger;

public class TemplateController
{

    private final Logger logger = new Logger( getClass() );

    private final FreekiConfig conf;

    private final Map<String, Template> templates = new HashMap<>();

    private final FreekiStore store;

    public TemplateController( final FreekiStore store, final FreekiConfig conf )
    {
        this.store = store;
        this.conf = conf;
        loadTemplates();
    }

    private void loadTemplates()
    {
        final File dir = conf.getTemplatesDir();
        if ( dir.isDirectory() )
        {
            final File[] files = dir.listFiles();
            for ( final File file : files )
            {
                if ( file.getName()
                         .startsWith( "." ) )
                {
                    continue;
                }

                if ( file.isDirectory() )
                {
                    final String label = file.getName();
                    final File html = new File( file, label + ".html" );
                    final File script = new File( file, label + ".groovy" );
                    templates.put( label, new Template( label, html, script ) );
                }
            }
        }
    }

    public List<String> getTemplateLabels()
    {
        final List<String> labels = new ArrayList<>( templates.keySet() );
        Collections.sort( labels );

        return labels;
    }

    public File getTemplateHtml( final String label )
    {
        final Template template = templates.get( label );
        if ( template != null )
        {
            return template.getHtml();
        }

        return null;
    }

    public String runTemplateAction( final String label, final Map<String, String> params )
        throws TemplateException
    {
        final Template template = templates.get( label );
        if ( template == null )
        {
            throw new TemplateException( "Cannot find template: '%s'", label );
        }

        final File templateFile = template.getScript();

        final Map<String, Object> map = new HashMap<String, Object>();
        map.putAll( params );

        GroovyClassLoader gcl = null;
        try
        {
            gcl = new GroovyClassLoader( Thread.currentThread()
                                               .getContextClassLoader() );
            TemplateAction action = null;
            try
            {
                final Class<?> scriptCls = gcl.parseClass( templateFile );
                action = TemplateAction.class.cast( scriptCls.newInstance() );
            }
            catch ( CompilationFailedException | IOException | InstantiationException | IllegalAccessException e )
            {
                throw new TemplateException( "Failed to read/compile/instantiate template action from script: %s. Reason: %s", e, templateFile,
                                             e.getMessage() );
            }

            logger.info( "Running action: %s from: %s\nwith params:\n\n%s", action, templateFile, params );
            return action.run( params, store );
        }
        finally
        {
            if ( gcl != null )
            {
                try
                {
                    gcl.close();
                }
                catch ( final IOException e )
                {
                    logger.error( "POTENTIAL RESOURCE LEAK: Failed to close GroovyClassLoader for template: %s. Reason: %s", e, label, e.getMessage() );
                }
            }
        }
    }

}
