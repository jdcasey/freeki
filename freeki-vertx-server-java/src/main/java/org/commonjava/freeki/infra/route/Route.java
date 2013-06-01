package org.commonjava.freeki.infra.route;

import org.vertx.java.core.http.HttpServerRequest;

public interface Route
{

    void handle( Method method, HttpServerRequest request )
        throws Exception;

    Iterable<String> patterns();

    Iterable<Method> methods();

}
