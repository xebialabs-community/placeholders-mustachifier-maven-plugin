package com.xebialabs.maven.mustache.provider;

public class DefaultValueProvider implements ValueProvider {

    @Override
    public String getValue(final String key, final String defaultValue) {
        return defaultValue;
    }
}
