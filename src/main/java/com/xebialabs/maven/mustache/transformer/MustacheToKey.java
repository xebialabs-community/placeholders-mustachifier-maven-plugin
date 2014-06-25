package com.xebialabs.maven.mustache.transformer;


public class MustacheToKey extends DefaultMustacheCollector {

    public MustacheToKey(final String beforeDelimiter, final String afterDelimiter, final String valueSeparator) {
        super(afterDelimiter, beforeDelimiter, valueSeparator);
    }

    @Override
    public Object get(final Object ctx, final String name) throws Exception {
        return beforeDelimiter + name.split(valueSeparator)[0] + afterDelimiter;
    }

}
