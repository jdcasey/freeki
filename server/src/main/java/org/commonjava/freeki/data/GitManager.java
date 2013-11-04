package org.commonjava.freeki.data;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.commonjava.freeki.conf.FreekiConfig;
import org.commonjava.util.logging.Logger;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RmCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revplot.PlotCommitList;
import org.eclipse.jgit.revplot.PlotLane;
import org.eclipse.jgit.revplot.PlotWalk;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.filter.AndTreeFilter;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;

public class GitManager
{

    private final Logger logger = new Logger( getClass() );

    private FreekiConfig config;

    private Git git;

    private int basepathLength;

    private String username;

    private String email;

    private Repository repo;

    protected GitManager()
    {
    }

    public GitManager( final FreekiConfig config )
        throws IOException, InvalidRemoteException, TransportException, GitAPIException
    {
        this.config = config;
        setupGit();
    }

    @PostConstruct
    public void setupGit()
        throws IOException, InvalidRemoteException, TransportException, GitAPIException
    {
        basepathLength = config.getContentDir()
                               .getPath()
                               .length() + 1;

        final File gitDir = config.getContentDir();
        final String cloneUrl = config.getCloneFrom();

        boolean checkCanonicalUpdate = false;
        if ( cloneUrl != null )
        {
            logger.info( "Cloning: %s into: %s", cloneUrl, gitDir );
            if ( gitDir.isDirectory() )
            {
                checkCanonicalUpdate = true;
            }

            Git.cloneRepository()
               .setURI( cloneUrl )
               .setDirectory( gitDir )
               .setRemote( "canonical" )
               .call();
        }

        final File dotGitDir = new File( gitDir, ".git" );
        final boolean create = cloneUrl == null && !dotGitDir.isDirectory();

        logger.info( "Setting up git manager for: %s", dotGitDir );
        repo = new FileRepositoryBuilder().readEnvironment()
                                          .setGitDir( dotGitDir )
                                          .build();

        username = repo.getConfig()
                       .getString( "user", null, "name" );

        if ( username == null )
        {
            username = System.getProperty( "user.name" );
        }

        email = repo.getConfig()
                    .getString( "user", null, "email" );

        if ( email == null )
        {
            email = username + "@" + InetAddress.getLocalHost()
                                                .getCanonicalHostName();
        }

        if ( create )
        {
            repo.create();
        }

        git = new Git( repo );

        if ( checkCanonicalUpdate )
        {
            final String canonical = getCanonicalUrl();
            if ( config.getCloneFrom()
                       .equals( canonical ) )
            {
                git.fetch()
                   .setRemote( "canonical" )
                   .call();
            }
            else
            {
                throw new IOException( "Invalid content detected! Canonical URL: " + canonical + " differs from clone-from URL: "
                    + config.getCloneFrom() );
            }
        }
    }

    public void addAndCommit( final String message, final File... files )
        throws IOException
    {
        addAndCommit( message, Arrays.asList( files ) );
    }

    public void addAndCommit( final String message, final Collection<File> files )
        throws IOException
    {
        try
        {
            final AddCommand add = git.add();
            final CommitCommand commit = git.commit();
            for ( final File f : files )
            {
                final String filepath = f.getPath()
                                         .substring( basepathLength );

                add.addFilepattern( filepath );

                // TODO: Is this needed? It was in there before the refactor to GitManager...
                //                commit.setOnly( filepath );
            }

            add.call();

            // TODO: Get the authorship info from somewhere...
            commit.setMessage( message )
                  .setAuthor( username, email )
                  .call();
        }
        catch ( final NoFilepatternException e )
        {
            throw new IOException( "Cannot add to git: " + e.getMessage(), e );
        }
        catch ( final GitAPIException e )
        {
            throw new IOException( "Cannot add to git: " + e.getMessage(), e );
        }
    }

    public void deleteAndCommit( final String message, final File... deleted )
        throws IOException
    {
        deleteAndCommit( message, Arrays.asList( deleted ) );
    }

    public void deleteAndCommit( final String message, final Collection<File> deleted )
        throws IOException
    {
        try
        {
            RmCommand rm = git.rm();
            CommitCommand commit = git.commit();

            for ( final File file : deleted )
            {
                final String filepath = file.getPath()
                                            .substring( basepathLength );

                rm = rm.addFilepattern( filepath );
                commit = commit.setOnly( filepath );
            }

            rm.call();

            // TODO: Get the authorship info from somewhere...
            commit.setMessage( message )
                  .setAuthor( username, email )
                  .call();
        }
        catch ( final NoFilepatternException e )
        {
            throw new IOException( "Cannot remove from git: " + e.getMessage(), e );
        }
        catch ( final GitAPIException e )
        {
            throw new IOException( "Cannot remove from git: " + e.getMessage(), e );
        }

    }

    // FIXME: Refine throws.
    public PlotCommit<PlotLane> getHeadCommit( final File f )
        throws Exception
    {
        final ObjectId oid = repo.resolve( "HEAD" );
        final PlotWalk pw = new PlotWalk( repo );
        final RevCommit rc = pw.parseCommit( oid );
        pw.markStart( rc );
        pw.setTreeFilter( AndTreeFilter.create( PathFilter.create( f.getPath()
                                                                    .substring( basepathLength ) ), TreeFilter.ANY_DIFF ) );

        final PlotCommitList<PlotLane> cl = new PlotCommitList<>();
        cl.source( pw );
        cl.fillTo( 1 );

        return cl.get( 0 );
        //        final PersonIdent ident = pc.getAuthorIdent();
        //        final String message = pc.getFullMessage();
        //        System.out.printf( "%s %s %s %s %s\n\n%s\n", ident.getName(), ident.getEmailAddress(), ident.getWhen(), ident.getTimeZone()
        //                                                                                                                     .getID(),
        //                           ident.getTimeZoneOffset(), message );
    }

    public void pullUpdates()
        throws IOException
    {
        try
        {
            git.pull()
               .setRebase( true )
               .call();
        }
        catch ( final GitAPIException e )
        {
            throw new IOException( "Cannot pull content updates via git: " + e.getMessage(), e );
        }
    }

    public void pushUpdates( final String user, final String password )
        throws IOException
    {
        final CredentialsProvider cp = new UsernamePasswordCredentialsProvider( user, password );
        try
        {
            git.push()
               .setCredentialsProvider( cp )
               .call();
        }
        catch ( final GitAPIException e )
        {
            throw new IOException( "Cannot push content updates via git: " + e.getMessage(), e );
        }
    }

    public String getOriginUrl()
    {
        return git.getRepository()
                  .getConfig()
                  .getString( "remote", "origin", "url" );
    }

    public String getCanonicalUrl()
    {
        return git.getRepository()
                  .getConfig()
                  .getString( "remote", "canonical", "url" );
    }
}
