package com.xebialabs.maven.mustache;

import com.google.common.collect.Maps;
import com.samskivert.mustache.DefaultCollector;
import com.samskivert.mustache.Mustache;

public class ValueSetterTransformer extends DefaultCollector implements Maps.EntryTransformer<String, String, String>, Mustache.VariableFetcher {

    final String beforeDelimiter;
    final String afterDelimiter;
    final String valueSeparator;

    public ValueSetterTransformer(final String beforeDelimiter, final String afterDelimiter, final String valueSeparator) {
        this.beforeDelimiter = beforeDelimiter;
        this.afterDelimiter = afterDelimiter;
        this.valueSeparator = valueSeparator;
    }

    @Override
    public String transformEntry(final String key, final String value) {
        return getCompiler().compile(value).execute(new Object());
    }


    @Override
    public Mustache.VariableFetcher createFetcher(final Object ctx, final String name) {
        return this;
    }

    @Override
    public Object get(final Object ctx, final String name) throws Exception {
        if (name.contains(valueSeparator))
            return name.split(valueSeparator)[1];
        else
            return beforeDelimiter + name + afterDelimiter;


    }

    private Mustache.Compiler getCompiler() {
        return Mustache.compiler()
                .withCollector(this)
                .withDelims(beforeDelimiter+" "+afterDelimiter)
                .escapeHTML(false)
                .standardsMode(true);
    }
}
