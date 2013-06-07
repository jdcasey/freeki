package org.commonjava.freeki.model;

import java.io.File;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.commonjava.freeki.infra.anno.RenderKey;

@RenderKey( "group" )
public class Group
    implements Comparable<Group>
{

    private String name;

    private SortedSet<ChildRef> children;

    public Group()
    {
    }

    public Group( final String name )
    {
        this.name = name;
    }

    public Group( final String name, final Collection<ChildRef> children )
    {
        this.children = new TreeSet<ChildRef>( children );
        this.name = name;
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

    public SortedSet<ChildRef> getChildren()
    {
        return children;
    }

    public void setChildren( final Collection<ChildRef> pages )
    {
        this.children = new TreeSet<ChildRef>( pages );
    }

    public static String nameFor( final String subgroup, final String fname )
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
        return n;
    }

    public void setChildren( final SortedSet<ChildRef> children )
    {
        this.children = children;
    }

    @Override
    public String toString()
    {
        return String.format( "Group [%s]", name );
    }

}
