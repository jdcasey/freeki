package org.commonjava.freeki.rest;

public enum PathParameter
{

    DIR, PAGE, PATH, TEMPLATE;

    public String param()
    {
        return name().toLowerCase();
    }

}
