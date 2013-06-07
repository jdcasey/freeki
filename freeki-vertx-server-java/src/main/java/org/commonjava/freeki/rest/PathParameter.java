package org.commonjava.freeki.rest;

public enum PathParameter
{

    DIR, PAGE;

    public String param()
    {
        return name().toLowerCase();
    }

}
