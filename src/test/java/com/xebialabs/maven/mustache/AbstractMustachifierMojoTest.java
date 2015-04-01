package com.xebialabs.maven.mustache;

import junit.framework.Assert;
import org.apache.maven.shared.model.fileset.FileSet;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class AbstractMustachifierMojoTest {

    final static String lineBreak = System.getProperty("line.separator");

    @Test
    public void testSaveSingleFile() throws Exception {
        MustachifierToValueMojo mojo = new MustachifierToValueMojo();
        mojo.setValueSeparator(":");
        mojo.setDelimiters("{{ }}");
        mojo.setFilesets(new ArrayList<FileSet>());
        mojo.saveSingleFile(new SingleFile("./target/test-classes/mustache/withplaceholders/config.properties", "./target/test-classes/mustache/withplaceholders/config-test.properties\\", false));
    }

    @Test
    public void testSaveSingleFileWithFiltering() throws Exception {
        MustachifierToValueMojo mojo = new MustachifierToValueMojo();
        mojo.setValueSeparator(":");
        mojo.setDelimiters("{{ }}");
        mojo.setFilesets(new ArrayList<FileSet>());
        mojo.saveSingleFile(new SingleFile("./target/test-classes/mustache/withplaceholders/config.properties", "./target/test-classes/mustache/withplaceholders/config-filter.properties\\", true));
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