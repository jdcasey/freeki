/*
 * Copyright 2011-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.commonjava.freeki.route;

import static org.apache.commons.lang.StringUtils.join;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.commonjava.freeki.rest.Route;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;

/**
 * This class allows you to do route requests based on the HTTP verb and the request URI, in a manner similar
 * to <a href="http://www.sinatrarb.com/">Sinatra</a> or <a href="http://expressjs.com/">Express</a>.<p>
 * RouteMatcher also lets you extract paramaters from the request URI either a simple pattern or using
 * regular expressions for more complex matches. Any parameters extracted will be added to the requests parameters
 * which will be available to you in your request handler.<p>
 * It's particularly useful when writing REST-ful web applications.<p>
 * To use a simple pattern to extract parameters simply prefix the parameter name in the pattern with a ':' (colon).<p>
 * Different handlers can be specified for each of the HTTP verbs, GET, POST, PUT, DELETE etc.<p>
 * For more complex matches regular expressions can be used in the pattern. When regular expressions are used, the extracted
 * parameters do not have a name, so they are put into the HTTP request with names of param0, param1, param2 etc.<p>
 * Multiple matches can be specified for each HTTP verb. In the case there are more than one matching patterns for
 * a particular request, the first matching one will be used.<p>
 * Instances of this class are not thread-safe<p>
 * 
 * <p><b>NOTE:</b> This is a variant of {@link RouteMatcher} that has more advanced parameter-regex support.</p>
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author <a href="http://github.com/jdcasey">John Casey</a>
 */
public class FreekiRouteMatcher
    implements Handler<HttpServerRequest>
{

    private static final String PATH_SEG_PATTERN = "([^\\/]+)";

    private final Map<Method, List<PatternBinding>> bindings = new HashMap<>();

    private Handler<HttpServerRequest> noMatchHandler;

    @Override
    public void handle( final HttpServerRequest request )
    {
        System.out.printf( "REQUEST %s %s\n", request.method(), request.path() );

        final List<PatternBinding> bindings = this.bindings.get( Method.valueOf( request.method() ) );
        System.out.printf( "Available bindings:\n  %s\n", join( bindings, "\n  " ) );
        if ( bindings != null )
        {
            for ( final PatternBinding binding : bindings )
            {
                final Matcher m = binding.pattern.matcher( request.path() );
                if ( m.matches() )
                {
                    System.out.printf( "MATCH: %s\n", binding.handler );
                    final Map<String, String> params = new HashMap<>( m.groupCount() );
                    if ( binding.paramNames != null )
                    {
                        // Named params
                        int i = 1;
                        for ( final String param : binding.paramNames )
                        {
                            params.put( param, m.group( i ) );
                            i++;
                        }
                    }
                    else
                    {
                        // Un-named params
                        for ( int i = 0; i < m.groupCount(); i++ )
                        {
                            params.put( "param" + i, m.group( i + 1 ) );
                        }
                    }

                    System.out.printf( "PARAMS: %s\n", params );
                    request.params()
                           .set( params );

                    binding.handler.handle( request );
                    return;
                }
            }
        }
        if ( noMatchHandler != null )
        {
            noMatchHandler.handle( request );
        }
        else
        {
            // Default 404
            request.response()
                   .setStatusCode( 404 );
            request.response()
                   .end();
        }
    }

    /**
     * Specify a handler that will be called for all HTTP methods
     * @param pattern The simple pattern
     * @param handler The handler to call
     */
    public FreekiRouteMatcher add( final Route route )
    {
        for ( final Method method : route.methods() )
        {
            List<PatternBinding> b = bindings.get( method );
            if ( b == null )
            {
                b = new ArrayList<>();
                bindings.put( method, b );
            }

            for ( final String pattern : route.patterns() )
            {
                System.out.printf( "ADD Method: %s, Pattern: %s, Route: %s\n", method, pattern, route );
                addPattern( pattern, route, b );
            }
        }
        return this;
    }

    /**
     * Specify a handler that will be called when no other handlers match.
     * If this handler is not specified default behaviour is to return a 404
     * @param handler
     */
    public FreekiRouteMatcher noMatch( final Handler<HttpServerRequest> handler )
    {
        noMatchHandler = handler;
        return this;
    }

    private void addPattern( final String input, final Handler<HttpServerRequest> handler,
                             final List<PatternBinding> bindings )
    {
        // input is /:name/:path=(.+)/:page
        // route pattern is: /([^\\/]+)/(.+)/([^\\/]+)
        // group list is: [name, path, page], where index+1 == regex-group-number

        // We need to search for any :<token name> tokens in the String and replace them with named capture groups
        final Matcher m = Pattern.compile( ":([A-Za-z][A-Za-z0-9_]*)(=\\([^)]+\\))?" )
                                 .matcher( input );
        final StringBuffer sb = new StringBuffer();
        final List<String> groups = new ArrayList<>();
        while ( m.find() )
        {
            final String group = m.group( 1 );
            String pattern = m.group( 2 );
            if ( pattern == null )
            {
                pattern = PATH_SEG_PATTERN;
            }
            else
            {
                pattern = pattern.substring( 1 );
            }

            if ( groups.contains( group ) )
            {
                throw new IllegalArgumentException( "Cannot use identifier " + group
                    + " more than once in pattern string" );
            }

            m.appendReplacement( sb, pattern );

            groups.add( group );
        }
        m.appendTail( sb );
        final String regex = sb.toString();

        System.out.printf( "BIND regex: %s, groups: %s, route: %s\n", regex, groups, handler );

        final PatternBinding binding = new PatternBinding( Pattern.compile( regex ), groups, handler );
        bindings.add( binding );
    }

    private static class PatternBinding
    {
        final Pattern pattern;

        final Handler<HttpServerRequest> handler;

        final List<String> paramNames;

        private PatternBinding( final Pattern pattern, final List<String> paramNames,
                                final Handler<HttpServerRequest> handler )
        {
            this.pattern = pattern;
            this.paramNames = paramNames;
            this.handler = handler;
        }

        @Override
        public String toString()
        {
            return String.format( "Binding [pattern: %s, params: %s, handler: %s]", pattern, paramNames, handler );
        }
    }

}
