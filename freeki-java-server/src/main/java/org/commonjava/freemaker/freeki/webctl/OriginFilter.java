package org.commonjava.freemaker.freeki.webctl;

import java.io.IOException;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter( urlPatterns = { "/*" } )
public class OriginFilter
    implements Filter
{

    @Override
    public void init( final FilterConfig filterConfig )
        throws ServletException
    {
    }

    @Override
    public void doFilter( final ServletRequest request, final ServletResponse response, final FilterChain chain )
        throws IOException, ServletException
    {
        final HttpServletRequest req = (HttpServletRequest) request;
        final String origin = req.getHeader( "ORIGIN" );
        final String method = req.getMethod();

        chain.doFilter( request, response );

        if ( origin != null )
        {
            final URL u = new URL( origin );
            if ( u.getHost()
                  .equals( req.getLocalName() ) )
            {
                final HttpServletResponse resp = (HttpServletResponse) response;

                resp.setHeader( "Access-Control-Allow-Origin", origin );

                if ( "OPTIONS".equals( method ) )
                {
                    resp.setHeader( "Access-Control-Allow-Headers", "X-PINGOTHER" );
                    resp.setHeader( "Access-Control-Max-Age", "86400" );
                    resp.setHeader( "Access-Control-Allow-Methods", "POST, PUT, GET, DELETE, HEAD, OPTIONS" );
                }
            }
        }
    }

    @Override
    public void destroy()
    {
        // TODO Auto-generated method stub

    }

}
