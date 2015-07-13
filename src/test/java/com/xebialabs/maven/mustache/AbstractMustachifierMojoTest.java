package com.xebialabs.maven.mustache;

import junit.framework.Assert;
import org.apache.maven.shared.model.fileset.FileSet;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import static com.xebialabs.maven.mustache.MustachifierMojoTest.getPropertiesFromFile;
import static com.xebialabs.maven.mustache.MustachifierToValueMojoTest.assertProperties;
import static org.junit.Assert.*;

public class AbstractMustachifierMojoTest {

    final static String lineBreak = System.getProperty("line.separator");

    @Test
    public void testSaveSingleFile() throws Exception {
        final String outputFileName = "./target/test-classes/mustache/withplaceholders/config-test.properties";
        MustachifierToValueMojo mojo = new MustachifierToValueMojo();
        mojo.setValueSeparator(":");
        mojo.setDelimiters("{{ }}");
        mojo.setFilesets(new ArrayList<FileSet>());
        mojo.saveSingleFile(new SingleFile("./target/test-classes/mustache/withplaceholders/config.properties", outputFileName, false));
        Properties properties = getPropertiesFromFile(new File(outputFileName));
        assertProperties(properties);
    }

    @Test
    public void testSaveSingleFileWithFiltering() throws Exception {
        final String outputFileName = "./target/test-classes/mustache/withplaceholders/config-filter.properties";
        MustachifierToValueMojo mojo = new MustachifierToValueMojo();
        mojo.setValueSeparator(":");
        mojo.setDelimiters("{{ }}");
        mojo.setFilesets(new ArrayList<FileSet>());
        mojo.saveSingleFile(new SingleFile("./target/test-classes/mustache/withplaceholders/config.properties", outputFileName, true));
        Properties properties = getPropertiesFromFile(new File(outputFileName));
        assertEquals(5, properties.size());
    }


    @Test
    public void testFilterMustacheOnly() throws Exception {
        MustachifierToValueMojo mojo = new MustachifierToValueMojo();
        mojo.setValueSeparator(":");
        mojo.setDelimiters("{{ }}");

        String content = "Boo=2"+lineBreak+"test={{fdfsf}}"+lineBreak;
        String result = mojo.filterMustacheOnly(content);
        System.err.println("result: "+result);
        Assert.assertTrue(result.contains("test"));
        Assert.assertFalse(result.contains("Boo"));
    }
}