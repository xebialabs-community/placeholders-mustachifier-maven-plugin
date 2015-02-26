package com.xebialabs.maven.mustache.transformer;

import com.samskivert.mustache.Mustache;

import com.xebialabs.maven.mustache.provider.ValueProvider;

public class MustacheToValue extends DefaultMustacheCollector implements Mustache.VariableFetcher {

    final ValueProvider valueProvider;

    public MustacheToValue(final String beforeDelimiter, final String afterDelimiter, final String valueSeparator, final ValueProvider valueProvider) {
        super(afterDelimiter, beforeDelimiter, valueSeparator);
        this.valueProvider = valueProvider;
    }

    @Override
    public Object get(final Object ctx, final String name) throws Exception {
        if (name.contains(valueSeparator)) {
            final int index = name.indexOf(valueSeparator);
            return valueProvider.getValue(name.split(valueSeparator)[0], index == name.length() - 1 ? "" : name.substring(index + 1));
        }
        else
            return valueProvider.getValue(name, beforeDelimiter + name + afterDelimiter);
    }

}
