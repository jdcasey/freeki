package org.commonjava.freeki.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.commonjava.freeki.testutil.FileFixture;
import org.junit.Test;

public class PageTest
{

    private static final String DIR = "pages/";

    public FileFixture fix = new FileFixture();

    @Test
    public void parsingSimplePageRemovesHeaderComment()
        throws Exception
    {
        final String id = "simple-with-comments";
        final File pgFile = fix.getClasspathFile( DIR + id + ".md" );
        assertThat( pgFile, notNullValue() );
        assertThat( pgFile.exists(), equalTo( true ) );

        String content = FileUtils.readFileToString( pgFile );

        assertThat( content.startsWith( "<!---" ), equalTo( true ) );
        assertThat( content.contains( "--->" ), equalTo( true ) );

        final Page pg = new Page( "path/to", id, content, System.currentTimeMillis() );

        content = pg.getContent();
        assertThat( content.startsWith( "<!---" ), equalTo( false ) );
        assertThat( content.contains( "--->" ), equalTo( false ) );
    }

}
