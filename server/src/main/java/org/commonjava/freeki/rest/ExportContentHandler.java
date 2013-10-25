package org.commonjava.freeki.rest;

import static org.apache.commons.lang.StringUtils.join;
import static org.commonjava.freeki.rest.PathParameter.PATH;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.commonjava.freeki.conf.FreekiConfig;
import org.commonjava.freeki.data.FreekiStore;
import org.commonjava.util.logging.Logger;
import org.commonjava.vertx.vabr.Method;
import org.commonjava.vertx.vabr.RouteHandler;
import org.commonjava.vertx.vabr.anno.Route;
import org.commonjava.vertx.vabr.anno.Routes;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.impl.DefaultVertx;

public class ExportContentHandler
    implements RouteHandler
{

    private final Logger logger = new Logger( getClass() );

    private final Map<String, File> exports = new ConcurrentHashMap<>();

    private final Map<String, Exporter> exporters = new ConcurrentHashMap<>();

    private final Set<String> downloadingPaths = new HashSet<>();

    private final ExecutorService executor = Executors.newFixedThreadPool( 12, new ThreadFactory()
    {
        private int counter = 0;

        @Override
        public Thread newThread( final Runnable r )
        {
            final Thread t = new Thread( r );
            t.setName( "export-" + counter++ );
            t.setPriority( 7 );
            t.setDaemon( true );

            return t;
        }
    } );

    private final Vertx vertx = new DefaultVertx();

    //    private final ConcurrentHashMap<String, HttpClient> clients = new ConcurrentHashMap<>();

    private final FreekiConfig config;

    private final FreekiStore store;

    public ExportContentHandler( final FreekiConfig config, final FreekiStore store )
    {
        this.config = config;
        this.store = store;
    }

    /* @formatter:off */
    @Routes( { 
        @Route( path = "/export/zip", method = Method.GET ),
        @Route( path = "/export/zip/:path=(.+)", method = Method.GET )
    } )
    /* @formatter:on */
    public void zip( final HttpServerRequest req )
        throws Exception
    {
        req.endHandler( new Handler<Void>()
        {
            @Override
            public void handle( final Void event )
            {
                final String path = req.params()
                                       .get( PATH.param() );
                final File exported = exports.get( path );
                if ( exported != null )
                {
                    req.response()
                       .headers()
                       .add( "Content-Type", "application/zip" );
                    req.response()
                       .setStatusMessage( "OK" )
                       .setStatusCode( 200 )
                       .sendFile( exported.getAbsolutePath() )
                       .end();
                    return;
                }

                Exporter exporter = exporters.get( path );
                if ( exporter == null )
                {
                    String seedUrl;
                    try
                    {
                        seedUrl = req.absoluteURI()
                                     .toURL()
                                     .toExternalForm()
                                     .replace( "/export/zip", "/wiki" );
                    }
                    catch ( final MalformedURLException e )
                    {
                        logger.error( "Cannot format target URL from: %s. Reason: %s", e, req.absoluteURI(), e.getMessage() );
                        req.response()
                           .setStatusCode( 500 )
                           .setStatusMessage( "Error while exporting content" )
                           .setChunked( true )
                           .write( e.getMessage() )
                           .end();
                        return;
                    }

                    exporter =
                        new Exporter( path, seedUrl, exporters, exports, config.getExportsDir(), store, vertx, /*clients,*/executor,
                                      downloadingPaths );
                    executor.execute( exporter );
                    exporters.put( path, exporter );
                }

                if ( exporter.hasError() )
                {
                    req.response()
                       .setStatusCode( 500 )
                       .setStatusMessage( "Error while exporting content" )
                       .setChunked( true )
                       .write( exporter.getError()
                                       .getMessage() )
                       .end();
                    return;
                }
                else if ( !exporter.isFinished() )
                {
                    req.response()
                       .headers()
                       .add( "Refresh", "5;" + req.absoluteURI() );
                    req.response()
                       .setStatusCode( 202 )
                       .setStatusMessage( "Export is running" )
                       .end();
                    return;
                }

                req.response()
                   .setChunked( true )
                   .setStatusCode( 500 )
                   .setStatusMessage( "Unknown export error" )
                   .write( "Exporter is present and marked as finished without error, but has not contributed exported content. Please try again." )
                   .end();
            }
        } );
    }

    public static final class Exporter
        implements Runnable
    {
        private final Logger logger = new Logger( getClass() );

        private final Map<String, Exporter> exporters;

        private final String myBasePath;

        private final Map<String, File> exported;

        private final File exportsBasedir;

        private final Set<String> downloadingPaths;

        private final Set<String> localDownloadingPaths = new HashSet<>();

        private Throwable error;

        private boolean finished;

        private final String seed;

        private ExportCommunications comms;

        private final FreekiStore store;

        private final ExecutorService executor;

        private final Vertx vertx;

        //        private final ConcurrentHashMap<String, HttpClient> clients;

        public Exporter( final String myBasePath, final String seedUrl, final Map<String, Exporter> exporters, final Map<String, File> exported,
                         final File exportsBasedir, final FreekiStore store,
                         final Vertx vertx/*, final ConcurrentHashMap<String, HttpClient> clients*/, final ExecutorService executor,
                         final Set<String> downloadingPaths )
        {
            this.myBasePath = myBasePath;
            this.seed = seedUrl;
            this.exporters = exporters;
            this.exported = exported;
            this.exportsBasedir = exportsBasedir;
            this.store = store;
            this.vertx = vertx;
            //            this.clients = clients;
            this.executor = executor;
            this.downloadingPaths = downloadingPaths;
        }

        public Throwable getError()
        {
            if ( error != null )
            {
                return error;
            }
            else if ( comms != null && !comms.errors.isEmpty() )
            {
                final StringWriter sw = new StringWriter();
                final PrintWriter pw = new PrintWriter( sw );
                int idx = 0;

                for ( final Entry<String, Throwable> entry : comms.errors.entrySet() )
                {
                    final String path = entry.getKey();
                    final Throwable error = entry.getValue();

                    pw.printf( "%d. For path: '%s'\n\n", idx++, path );
                    error.printStackTrace( pw );
                    pw.println();
                    pw.println();
                }

                error = new Exception( sw.toString() );
                return error;
            }

            return null;
        }

        public boolean isFinished()
        {
            return finished;
        }

        public boolean hasError()
        {
            return error != null || ( comms != null && !comms.errors.isEmpty() );
        }

        @Override
        public void run()
        {
            try
            {
                try
                {
                    final URL seedUrl = new URL( seed );
                    final LinkedList<URL> todo = new LinkedList<URL>();
                    todo.add( seedUrl );

                    comms =
                        new ExportCommunications( todo, exportsBasedir, seedUrl, myBasePath, store, vertx, /*clients,*/downloadingPaths,
                                                  localDownloadingPaths );

                    for ( int i = 0; i < 4; i++ )
                    {
                        executor.execute( new ExportCrawler( comms ) );
                    }

                    exported.put( myBasePath, new File( exportsBasedir, myBasePath ) );
                    exporters.remove( myBasePath );
                }
                catch ( final MalformedURLException e )
                {
                    logger.error( "Cannot format target URL from: %s. Reason: %s", e, seed, e.getMessage() );
                    error = e;
                    return;
                }
            }
            finally
            {
                synchronized ( downloadingPaths )
                {
                    downloadingPaths.removeAll( localDownloadingPaths );
                    downloadingPaths.notifyAll();
                }
            }
        }
    }

    public static final class ExportCommunications
    {
        public final File exportBasedir;

        public final URL seedUrl;

        public final String pathBase;

        public final Set<String> downloadingPaths;

        public final Map<String, Throwable> errors = new HashMap<>();

        public final FreekiStore store;

        public final Set<String> localDownloadingPaths;

        public final LinkedList<URL> todo;

        public final Vertx vertx;

        //
        //        public final ConcurrentHashMap<String, HttpClient> clients;

        public ExportCommunications( final LinkedList<URL> todo, final File exportBasedir, final URL seedUrl, final String pathBase,
                                     final FreekiStore store, final Vertx vertx/*, final ConcurrentHashMap<String, HttpClient> clients*/,
                                     final Set<String> downloadingPaths, final Set<String> localDownloadingPaths )
        {
            this.todo = todo;
            this.exportBasedir = exportBasedir;
            this.seedUrl = seedUrl;
            this.pathBase = pathBase;
            this.store = store;
            this.vertx = vertx;
            //            this.clients = clients;
            this.downloadingPaths = downloadingPaths;
            this.localDownloadingPaths = localDownloadingPaths;
        }
    }

    public static final class ExportCrawler
        implements Runnable
    {

        private final Logger logger = new Logger( getClass() );

        private final ExportCommunications ed;

        private Thread myThread;

        private boolean stop;

        public ExportCrawler( final ExportCommunications ed )
        {
            this.ed = ed;
        }

        public void stop()
        {
            stop = true;
            myThread.interrupt();
        }

        @Override
        public void run()
        {
            myThread = Thread.currentThread();

            while ( !stop )
            {
                URL url;
                synchronized ( ed.todo )
                {
                    while ( ed.todo.isEmpty() )
                    {
                        try
                        {
                            ed.todo.wait( 500 );
                        }
                        catch ( final InterruptedException e )
                        {
                            return;
                        }
                    }

                    url = ed.todo.removeFirst();
                }

                if ( shouldVisit( url ) )
                {
                    visit( url );
                }
            }
        }

        public boolean shouldVisit( final URL url )
        {
            logger.info( "Checking: %s\nVs: %s", url, ed.seedUrl );

            final boolean relevantWikiPath = url.toExternalForm()
                                                .startsWith( ed.seedUrl.toExternalForm() );

            final String seedDomain = toDomain( ed.seedUrl );
            final String domain = toDomain( url );

            final boolean domainMatches = domain.equals( seedDomain );

            final boolean staticUrl = url.getPath()
                                         .startsWith( "/static/" );

            logger.info( "Relevant wiki path? %s\nDomain matches ('%s' vs. '%s')? %s\nStatic URL? %s", relevantWikiPath, domain, seedDomain,
                         domainMatches, staticUrl );

            if ( relevantWikiPath || ( domainMatches && staticUrl ) )
            {
                synchronized ( ed.localDownloadingPaths )
                {
                    final boolean downloading = ed.localDownloadingPaths.contains( url.getPath() );
                    logger.info( "Already downloading in another (local) crawler thread? %s", downloading );
                    if ( downloading )
                    {
                        return false;
                    }

                    ed.localDownloadingPaths.add( url.getPath() );
                    ed.localDownloadingPaths.notifyAll();
                }

                synchronized ( ed.downloadingPaths )
                {
                    final boolean downloading = ed.downloadingPaths.contains( url.getPath() );
                    logger.info( "Already downloading in another crawler thread? %s", downloading );
                    if ( downloading )
                    {
                        return false;
                    }

                    ed.downloadingPaths.add( url.getPath() );
                    ed.downloadingPaths.notifyAll();
                    return true;
                }
            }

            return false;
        }

        private String toDomain( final URL url )
        {
            String domain = url.getHost();
            if ( url.getPort() > 0 )
            {
                domain += ":" + url.getPort();
            }

            return domain;
        }

        public void visit( final URL pageUrl )
        {
            HttpClient client;
            int port = pageUrl.getPort();
            if ( port < 1 )
            {
                port = pageUrl.getProtocol()
                              .equals( "https" ) ? 443 : 80;
            }

            client = ed.vertx.createHttpClient()
                             .setHost( pageUrl.getHost() )
                             .setPort( port )
                             .setReuseAddress( true )
                             .setSSL( pageUrl.getProtocol()
                                             .equals( "https" ) );

            client.get( pageUrl.toExternalForm(), new Handler<HttpClientResponse>()
            {
                @Override
                public void handle( final HttpClientResponse resp )
                {
                    resp.bodyHandler( new Handler<Buffer>()
                    {

                        @Override
                        public void handle( final Buffer data )
                        {
                            logger.info( "Visiting: %s", pageUrl );

                            String path = pageUrl.getPath();

                            if ( ed.store.hasGroup( path ) )
                            {
                                path = new File( path, "index.html" ).getPath();
                            }
                            else if ( path.indexOf( '.', path.lastIndexOf( '/' ) ) < 0 )
                            {
                                path += ".html";
                            }

                            final File f = new File( ed.exportBasedir, path );
                            final File d = f.getParentFile();
                            if ( !d.isDirectory() && !d.mkdirs() )
                            {
                                logger.error( "Failed to create export directory: %s", d );
                                ed.errors.put( path, new Exception( "Failed to create export sub-directory: " + d ) );
                            }

                            final Set<URL> newUrls = new HashSet<>();
                            try
                            {
                                if ( path.endsWith( ".html" ) )
                                {
                                    final Document doc = Jsoup.parse( new String( data.getBytes() ), pageUrl.toExternalForm() );

                                    for ( final Element link : doc.select( "a[href]" ) )
                                    {
                                        cleanLink( link, "abs:href", path, newUrls );
                                    }

                                    for ( final Element link : doc.select( "link[href]" ) )
                                    {
                                        cleanLink( link, "abs:href", path, newUrls );
                                    }

                                    for ( final Element link : doc.select( "[src]" ) )
                                    {
                                        cleanLink( link, "abs:src", path, newUrls );
                                    }

                                    FileUtils.writeStringToFile( f, doc.outerHtml() );
                                }
                                else if ( path.endsWith( ".css" ) )
                                {
                                    final String css = new String( data.getBytes() );
                                    final Matcher matcher = Pattern.compile( "(url\\()(.+)(\\);)" )
                                                                   .matcher( css );
                                    int start = 0;
                                    final StringBuilder sb = new StringBuilder();
                                    while ( matcher.find( start ) )
                                    {
                                        final String url = matcher.group( 2 );

                                        // FIXME: relativize url, sub into buffer.
                                        if ( sb.length() < 1 && matcher.start() > 0 )
                                        {
                                            sb.append( css.substring( 0, matcher.start() ) );
                                        }

                                        sb.append( matcher.group( 1 ) );
                                        sb.append( cleanUrl( url, path, newUrls ) );
                                        sb.append( matcher.group( 3 ) );

                                        start = matcher.end();
                                    }

                                    if ( start < css.length() - 1 )
                                    {
                                        sb.append( css.substring( start ) );
                                    }

                                    FileUtils.writeStringToFile( f, sb.toString() );
                                }
                                else
                                {
                                    FileUtils.writeByteArrayToFile( f, data.getBytes() );
                                }

                            }
                            catch ( final IOException e )
                            {
                                logger.error( "Error storing: %s. Reason: %s", e, f, e.getMessage() );
                                ed.errors.put( path, e );
                            }
                        }

                        private void cleanLink( final Element link, final String attr, final String path, final Set<URL> newUrls )
                            throws MalformedURLException
                        {
                            final String url = link.attr( attr );
                            final String relative = cleanUrl( url, path, newUrls );
                            if ( !relative.equals( url ) )
                            {
                                link.attr( attr, url );
                            }
                        }

                        private String cleanUrl( String url, final String path, final Set<URL> newUrls )
                            throws MalformedURLException
                        {
                            if ( url.startsWith( "/" ) )
                            {
                                url = relativize( url, path );

                                // ugly as hell, but relatively foolproof
                                newUrls.add( relativeUrl( pageUrl, url ) );
                            }
                            else if ( url.matches( "http[s]?://.+" ) )
                            {
                                newUrls.add( new URL( url ) );
                            }
                            else
                            {
                                newUrls.add( relativeUrl( pageUrl, "../" + url ) );
                            }

                            return url;
                        }

                        private URL relativeUrl( final URL pageUrl, final String url )
                            throws MalformedURLException
                        {
                            final String[] parts = ( pageUrl.toExternalForm() + url ).split( "/" );
                            final LinkedList<String> clean = new LinkedList<>();
                            for ( final String part : parts )
                            {
                                if ( part.equals( ".." ) && !clean.isEmpty() )
                                {
                                    clean.removeLast();
                                }
                                else
                                {
                                    clean.add( part );
                                }
                            }

                            return new URL( join( clean, "/" ) );
                        }

                        private String relativize( String url, final String path )
                        {
                            for ( int i = 0; i < path.split( "/" ).length; i++ )
                            {
                                url = "../" + url;
                            }

                            return url;
                        }

                    } );
                }

            } );

        }

    }
}
