package org.commonjava.freeki.infra.route;

import org.vertx.java.core.http.HttpServerRequest;

public abstract class RouteBinding
{

    private final String path;

    private final Method method;

    protected RouteBinding( final String path, final Method method )
    {
        this.path = path;
        this.method = method;
    }

    public String getPath()
    {
        return path;
    }

    public Method getMethod()
    {
        return method;
    }

    @Override
    public String toString()
    {
        return "Route [" + method.name() + " " + path + "]";
    }

    public abstract void handle( ApplicationRouter router, HttpServerRequest req )
        throws Exception;

}
