package com.xebialabs.maven.mustache.transformer;

import com.samskivert.mustache.DefaultCollector;
import com.samskivert.mustache.Mustache;

public abstract class DefaultMustacheCollector extends DefaultCollector implements Mustache.VariableFetcher {

    final String beforeDelimiter;
    final String afterDelimiter;
    final String valueSeparator;

    public DefaultMustacheCollector(final String afterDelimiter, final String beforeDelimiter, final String valueSeparator) {
        this.afterDelimiter = afterDelimiter;
        this.beforeDelimiter = beforeDelimiter;
        this.valueSeparator = valueSeparator;
    }

    @Override
    public Mustache.VariableFetcher createFetcher(final Object ctx, final String name) {
        return this;
    }

    private Mustache.Compiler getCompiler() {
        return Mustache.compiler()
                .withCollector(this)
                .withDelims(beforeDelimiter + " " + afterDelimiter)
                .escapeHTML(false)
                .standardsMode(true);
    }

    public String process(String content) {
        return getCompiler().compile(content).execute(new Object());
    }
}
