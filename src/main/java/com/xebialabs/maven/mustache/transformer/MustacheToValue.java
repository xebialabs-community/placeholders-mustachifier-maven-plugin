package com.xebialabs.maven.mustache.transformer;

import com.samskivert.mustache.Mustache;

import com.xebialabs.maven.mustache.provider.ValueProvider;

import org.apache.maven.plugin.MojoFailureException;

public class MustacheToValue extends DefaultMustacheCollector implements Mustache.VariableFetcher {

    final ValueProvider valueProvider;

    final Boolean mandatoryValues;

    public MustacheToValue(final String beforeDelimiter, final String afterDelimiter, final String valueSeparator,
                           final ValueProvider valueProvider, final Boolean mandatoryValues) {
        super(afterDelimiter, beforeDelimiter, valueSeparator);
        this.valueProvider = valueProvider;

        this.mandatoryValues = mandatoryValues;
    }

    @Override
    public Object get(final Object ctx, final String name) throws Exception {
        if (name.contains(valueSeparator)) {
            final int index = name.indexOf(valueSeparator);
            return valueProvider.getValue(name.split(valueSeparator)[0], index == name.length() - 1 ? "" : name.substring(index + 1));
        }
        else
            if (mandatoryValues) {
                return null;
            }
            return valueProvider.getValue(name, beforeDelimiter + name + afterDelimiter);
    }

}
