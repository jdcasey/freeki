package org.commonjava.freeki.model;

public class ChildRef
    implements Comparable<ChildRef>
{

    public enum ChildType
    {
        PAGE, GROUP;
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
        int comp = type.compareTo( other.type );
        if ( comp == 0 )
        {
            comp = label.compareTo( other.label );
        }

        return comp;
    }

}
