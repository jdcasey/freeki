package org.commonjava.freeki.rest;

public enum PathParameter
{

    DIR, PAGE, PATH;

    public String param()
    {
        return name().toLowerCase();
    }

}
