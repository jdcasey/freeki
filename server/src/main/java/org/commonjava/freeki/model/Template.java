package org.commonjava.freeki.model;

import java.io.File;

public class Template
{

    private final String label;

    private final File html;

    private final File script;

    public Template( final String label, final File html, final File script )
    {
        this.label = label;
        this.html = html;
        this.script = script;
    }

    public String getLabel()
    {
        return label;
    }

    public File getHtml()
    {
        return html;
    }

    public File getScript()
    {
        return script;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( label == null ) ? 0 : label.hashCode() );
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
        final Template other = (Template) obj;
        if ( label == null )
        {
            if ( other.label != null )
            {
                return false;
            }
        }
        else if ( !label.equals( other.label ) )
        {
            return false;
        }
        return true;
    }

}
