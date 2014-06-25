package com.xebialabs.maven.mustache;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import com.xebialabs.maven.mustache.transformer.MustacheGenerate;

import static com.google.common.collect.Maps.fromProperties;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.transformEntries;


/**
 * Mustachify a Java property file.
 *
 * @author Benoit Moussaud
 * @goal mustache-property-generate
 * @phase compile
 * @threadSafe
 */
public class PropertyFileMustachifierMojo extends AbstractMustachifierMojo {

    @Override
    protected String transform(final String content) {

        Properties properties = new Properties();
        try {
            properties.load(new StringReader(content));
        } catch (IOException e) {
            throw new RuntimeException("Cannot read the content " + content, e);
        }

        Map<String, String> processedEntries = newHashMap(transformEntries(fromProperties(properties),
                new MustacheGenerate(getBeforeDelimiter(), getAfterDelimiter())));
        properties.putAll(processedEntries);

        StringWriter writer = new StringWriter();
        properties.list(new PrintWriter(writer));
        return writer.getBuffer().toString();
    }

}
