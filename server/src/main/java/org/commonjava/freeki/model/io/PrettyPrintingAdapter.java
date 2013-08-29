package org.commonjava.freeki.model.io;

import org.commonjava.web.json.ser.WebSerializationAdapter;

import com.google.gson.GsonBuilder;

public class PrettyPrintingAdapter
    implements WebSerializationAdapter
{

    @Override
    public void register( final GsonBuilder gsonBuilder )
    {
        gsonBuilder.setPrettyPrinting();
    }

}
