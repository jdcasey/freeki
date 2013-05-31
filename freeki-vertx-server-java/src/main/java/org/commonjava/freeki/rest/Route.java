package org.commonjava.freeki.rest;

import org.commonjava.freeki.route.Method;
import org.vertx.java.core.http.HttpServerRequest;

public interface Route
{

    void handle( Method method, HttpServerRequest request );

    Iterable<String> patterns();

    Iterable<Method> methods();

}
