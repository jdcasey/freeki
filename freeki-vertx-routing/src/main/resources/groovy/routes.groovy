package ${pkg};

import org.commonjava.freeki.infra.route.ApplicationRouter;
import org.commonjava.freeki.infra.route.RouteBinding;
import org.commonjava.freeki.infra.route.Method;
import org.commonjava.freeki.infra.route.AbstractRouteCollection;
import org.vertx.java.core.http.HttpServerRequest;

public final class Routes
    extends AbstractRouteCollection
{

    public Routes()
    {<% routes.each { %>
        bind( new RouteBinding( "${it.httpPath}", Method.${it.httpMethod} )
        {
            public void handle( ApplicationRouter router, HttpServerRequest req )
                throws Exception
            {
                router.getResourceInstance( ${it.qualifiedClassname}.class ).${it.methodname}( req );
            }
        } );<% } %>
    }

}
