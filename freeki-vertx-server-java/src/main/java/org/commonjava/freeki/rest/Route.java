package org.commonjava.freeki.rest;

import org.commonjava.freeki.route.Method;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

public interface Route
    extends Handler<HttpServerRequest>
{

    Iterable<String> patterns();

    Iterable<Method> methods();

}
