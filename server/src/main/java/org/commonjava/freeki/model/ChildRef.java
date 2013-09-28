package org.commonjava.freeki.model;

public class ChildRef
    implements Comparable<ChildRef>
{

    public enum ChildType
    {
        GROUP, PAGE;
    }

    private ChildType type;

    private String label;

    private String id;

    public ChildRef()
    {
    }

    public ChildRef( final ChildType type, final String label, final String id )
    {
        this.type = type;
        this.label = label;
        this.id = id;
    }

    public ChildType getType()
    {
        return type;
    }

    public String getLabel()
    {
        return label;
    }

    public String getId()
    {
        return id;
    }

    public void setLabel( final String label )
    {
        this.label = label;
    }

    public void setId( final String id )
    {
        this.id = id;
    }

    @Override
    public int compareTo( final ChildRef other )
    {
        if ( "README".equals( label ) )
        {
            return -1;
        }
        else if ( "README".equals( other.label ) )
        {
            return 1;
        }

        int comp = label.compareTo( other.label );
        if ( comp == 0 )
        {
            comp = type.compareTo( other.type );
        }

        return comp;
    }

    @Override
    public String toString()
    {
        return String.format( "ChildRef [type=%s, label=%s, id=%s]", type, label, id );
    }

}
