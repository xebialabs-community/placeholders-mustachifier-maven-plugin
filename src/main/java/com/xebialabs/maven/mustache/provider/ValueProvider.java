package com.xebialabs.maven.mustache.provider;


public interface ValueProvider {

    String getValue(String key, String defaultValue);
}
