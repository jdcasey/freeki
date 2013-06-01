package org.commonjava.freeki.infra.render;

import org.commonjava.freeki.util.ContentType;

public interface ContentRenderer
{

    ContentType[] getContentTypes();

    String render( Object data )
        throws RenderingException;

}
