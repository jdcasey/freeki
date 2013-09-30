/*******************************************************************************
 * Copyright (C) 2013 John Casey.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.commonjava.freeki.store;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.commonjava.freeki.conf.FreekiConfig;
import org.commonjava.freeki.data.FreekiStore;
import org.commonjava.freeki.model.Page;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revplot.PlotLane;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FreekiStoreTest
{

    private FreekiStore store;

    private File dir;

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Before
    public void setup()
        throws Exception
    {
        dir = temp.newFolder( "freeki" );
        store = new FreekiStore( new FreekiConfig( dir )/*, new JsonSerializer( new PrettyPrintingAdapter() )*/);

        final Page pg = new Page( "mygroup", "page-id", "# Page Id\n\n## This is a header", "Page Id", System.currentTimeMillis(), "John Casey" );
        store.storePage( pg );
    }

    @Test
    public void getLogForValidFile()
        throws Exception
    {
        final PlotCommit<PlotLane> log = store.getHeadCommit( new File( dir, "mygroup/page-id.md" ) );
        assertThat( log, notNullValue() );
        final PersonIdent ident = log.getAuthorIdent();
        assertThat( ident, notNullValue() );
        assertThat( ident.getName(), notNullValue() );
        assertThat( ident.getEmailAddress(), notNullValue() );
        assertThat( ident.getWhen(), notNullValue() );

        final String message = log.getFullMessage();
        assertThat( message, notNullValue() );

        System.out.printf( "%s %s %s %s %s\n\n%s\n", ident.getName(), ident.getEmailAddress(), ident.getWhen(), ident.getTimeZone()
                                                                                                                     .getID(),
                           ident.getTimeZoneOffset(), message );
    }

    @Test
    public void getPageForValidFile()
        throws Exception
    {
        final Page page = store.getPage( "mygroup", "page-id" );
        assertThat( page, notNullValue() );
        assertThat( page.getTitle(), equalTo( "Page Id" ) );

        System.out.printf( "Page: %s\n\n%s\n\n", page, page.getContent() );
    }

    @Test
    public void getLogForInvalidFile()
        throws Exception
    {
        final PlotCommit<PlotLane> log = store.getHeadCommit( new File( dir, "path/to/group/page-missing.md" ) );
        assertThat( log, nullValue() );
    }

}
