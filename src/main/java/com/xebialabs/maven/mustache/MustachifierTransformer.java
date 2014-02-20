package com.xebialabs.maven.mustache;

import com.google.common.collect.Maps;
import com.samskivert.mustache.DefaultCollector;
import com.samskivert.mustache.Mustache;

public class MustachifierTransformer extends DefaultCollector implements Maps.EntryTransformer<String, String, String>, Mustache.VariableFetcher {

    final String beforeDelimiter;
    final String afterDelimiter;
    final String valueSeparator;
    final boolean generatePlaceholder;

    public MustachifierTransformer(final String beforeDelimiter, final String afterDelimiter, final String valueSeparator, final boolean generatePlaceholder) {
        this.beforeDelimiter = beforeDelimiter;
        this.afterDelimiter = afterDelimiter;
        this.valueSeparator = valueSeparator;
        this.generatePlaceholder = generatePlaceholder;
    }

    @Override
    public String transformEntry(final java.lang.String key, final java.lang.String value) {
        final String execute = getCompiler().compile(value).execute(new Object());
        if (execute.contains(beforeDelimiter) && execute.contains(afterDelimiter)) {
            return execute;
        }
        if (generatePlaceholder) {
            // No placeholder found, build the value based on the key
            return beforeDelimiter + key + afterDelimiter;
        } else {
            return value;
        }
    }


    @Override
    public Mustache.VariableFetcher createFetcher(final Object ctx, final String name) {
        return this;
    }

    @Override
    public Object get(final Object ctx, final String name) throws Exception {
        return beforeDelimiter + name.split(valueSeparator)[0] + afterDelimiter;
    }

    private Mustache.Compiler getCompiler() {
        return Mustache.compiler()
                .withCollector(this)
                .withDelims(beforeDelimiter + " " + afterDelimiter)
                .escapeHTML(false)
                .standardsMode(true);
    }
}
