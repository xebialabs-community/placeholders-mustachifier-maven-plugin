package com.xebialabs.maven.mustache.transformer;

import com.samskivert.mustache.Mustache;

public class MustacheToValue extends DefaultMustacheCollector implements Mustache.VariableFetcher {

    public MustacheToValue(final String beforeDelimiter, final String afterDelimiter, final String valueSeparator) {
        super(afterDelimiter, beforeDelimiter, valueSeparator);
    }

    @Override
    public Object get(final Object ctx, final String name) throws Exception {
        if (name.contains(valueSeparator))
            return name.split(valueSeparator)[1];
        else
            return beforeDelimiter + name + afterDelimiter;
    }

}
