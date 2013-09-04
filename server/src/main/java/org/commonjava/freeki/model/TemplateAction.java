package org.commonjava.freeki.model;

import java.util.Map;

import org.commonjava.freeki.data.FreekiStore;
import org.commonjava.freeki.data.TemplateException;

public interface TemplateAction
{

    String run( Map<String, String> params, FreekiStore store )
        throws TemplateException;

}
