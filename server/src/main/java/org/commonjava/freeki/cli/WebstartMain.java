package org.commonjava.freeki.cli;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import org.eclipse.jgit.api.errors.GitAPIException;

public class WebstartMain
{

    public static void main( final String[] args )
        throws Exception
    {
        EventQueue.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    System.out.println( "Starting web frame" );
                    new ConsoleFrame( args );
                }
                catch ( final Exception e )
                {
                    e.printStackTrace();
                }
            }
        } );
    }

    private static final class ConsoleFrame
        extends JFrame
    {
        private static final long serialVersionUID = 1L;

        private final JTextPane pane;

        private final JScrollPane scrollPane;

        private final JPanel panel;

        private final Main main;

        ConsoleFrame( final String[] args )
            throws Exception
        {
            pane = new JTextPane();
            scrollPane = new JScrollPane( pane );
            panel = new JPanel();

            setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            setBounds( 100, 100, 950, 600 );
            panel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
            panel.setLayout( new BorderLayout( 0, 0 ) );
            setContentPane( panel );

            pane.setEditable( false );
            pane.setContentType( "text/html" );
            pane.setText( "" );

            panel.add( scrollPane, BorderLayout.CENTER );

            final MessageConsole console = new MessageConsole( pane );
            console.redirectErr( Color.red, System.err );
            console.redirectOut( Color.black, System.out );
            console.setMessageLines( 5000 );

            setVisible( true );

            main = new Main( args );
            new Thread( new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        main.run();
                    }
                    catch ( IOException | GitAPIException e )
                    {
                        e.printStackTrace();
                        main.stop();
                    }
                }
            } ).start();
        }
    }

}
