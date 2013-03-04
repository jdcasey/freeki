package org.commonjava.freemaker.freeki.model;

import java.io.File;

public class Group
    implements Comparable<Group>
{

    private String name;

    public Group()
    {
    }

    public Group( final String subgroup, final String fname )
    {
        String n = fname;
        if ( subgroup != null )
        {
            n = new File( subgroup, fname ).getPath();
        }

        if ( n.startsWith( "/" ) )
        {
            n = n.substring( 1 );
        }

        this.name = n;
    }

    public String getName()
    {
        return name;
    }

    public void setName( final String name )
    {
        this.name = name;
    }

    @Override
    public int compareTo( final Group other )
    {
        return name.compareTo( other.name );
    }

}
