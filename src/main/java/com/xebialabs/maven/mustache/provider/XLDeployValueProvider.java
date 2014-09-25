package com.xebialabs.maven.mustache.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import com.google.common.io.Closeables;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import static java.lang.String.format;


public class XLDeployValueProvider implements ValueProvider {

    private String username;

    private String password;

    private String url;

    private String context = "deployit";

    private String environment;

    private Properties properties = new Properties();

    public XLDeployValueProvider() {
    }

    public XLDeployValueProvider(final String path) {
    }

    @Override
    public String getValue(final String key, final String defaultValue) {
        return getProperties().getProperty(key, defaultValue);
    }

    private Properties getProperties() {
        if (properties.isEmpty()) {
            CredentialsProvider provider = new BasicCredentialsProvider();
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(getUsername(), getPassword());
            provider.setCredentials(AuthScope.ANY, credentials);

            CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

            String requestURL = format("%s/%s/deployment/dictionary?environment=%s", url, context, environment);
            HttpGet get = new HttpGet(requestURL);

            // Execute HTTP request
            try {
                HttpResponse response = client.execute(get);
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                XStream xstream = new XStream();
                xstream.processAnnotations(MapHolder.class);

                MapHolder mapHolder = (MapHolder) xstream.fromXML(rd);

                for (MapEntry entry : mapHolder.entries) {
                    properties.put(entry.getKey(), entry.getValue());
                }
            } catch (IOException e) {
                throw new RuntimeException(format("Failed to execute %s", requestURL));
            } finally {
                Closeables.closeQuietly(client);
            }
        }

        return properties;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getContext() {
        return context;
    }

    public void setContext(final String context) {
        this.context = context;
    }


    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(final String environment) {
        this.environment = environment;
    }

    @XStreamAlias("map")
    public static class MapHolder {

        @XStreamImplicit
        public List<MapEntry> entries;
    }


    @XStreamAlias("entry")
    public static class MapEntry {

        @XStreamImplicit
        private List<String> content;

        String getKey() {
            return content.iterator().next();
        }

        String getValue() {
            final Iterator<String> iterator = content.iterator();
            iterator.next();
            return iterator.next();
        }
    }
}
