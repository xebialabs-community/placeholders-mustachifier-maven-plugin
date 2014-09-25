package com.xebialabs.maven.mustache.provider;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import com.google.common.io.Closeables;

public class PropertyFileValueProvider implements ValueProvider {

    private Properties properties;

    private String path;

    public PropertyFileValueProvider() {

    }

    @Override
    public String getValue(final String key, final String defaultValue) {
        return getProperties(path).getProperty(key, defaultValue);
    }

    private Properties getProperties(final String path) {
        if (properties == null) {
            properties = new Properties();
            FileReader reader = null;
            try {
                reader = new FileReader(path);
                properties.load(reader);
            } catch (IOException e) {
                throw new RuntimeException("Cannot read the content " + path, e);
            } finally {
                Closeables.closeQuietly(reader);
            }
        }
        return properties;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }
}
