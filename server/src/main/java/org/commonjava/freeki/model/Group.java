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
package org.commonjava.freeki.model;

import java.io.File;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.commonjava.freeki.infra.anno.RenderKey;

@RenderKey( key = "group", contentTemplate = "${data.name}" )
public class Group
    implements Comparable<Group>
{

    private String name;

    private String parent;

    private SortedSet<ChildRef> children;

    public Group()
    {
    }

    public Group( final String name )
    {
        this.name = name;
        this.parent = new File( name ).getParent();
    }

    public Group( final String name, final Collection<ChildRef> children )
    {
        this.children = new TreeSet<ChildRef>( children );
        this.name = name;
        this.parent = new File( name ).getParent();
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

    public String getParent()
    {
        return parent;
    }

    public void setParent( final String parent )
    {
        this.parent = parent;
    }

}
