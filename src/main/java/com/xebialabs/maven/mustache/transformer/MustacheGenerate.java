package com.xebialabs.maven.mustache.transformer;

import com.google.common.collect.Maps;

public class MustacheGenerate implements Maps.EntryTransformer<String, String, String> {

    final String beforeDelimiter;
    final String afterDelimiter;

    public MustacheGenerate(final String beforeDelimiter, final String afterDelimiter) {
        this.beforeDelimiter = beforeDelimiter;
        this.afterDelimiter = afterDelimiter;
    }

    @Override
    public String transformEntry(final String key, final String value) {
        return beforeDelimiter + key + afterDelimiter;
    }
}
