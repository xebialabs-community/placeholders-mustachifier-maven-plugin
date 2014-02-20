package com.xebialabs.maven.mustache;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Properties;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.shared.model.fileset.FileSet;
import org.codehaus.plexus.util.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import com.google.common.io.Files;

@RunWith(BlockJUnit4ClassRunner.class)
public class PropertyFileMustachifierMojoTest extends AbstractMojoTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testSimpleConfigurationWithSpecialEntries() throws Exception {
        final File targetFile = new File("./target/test-classes/mustache/config.properties");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getParent());
        fs.setIncludes(Collections.singletonList(targetFile.getName()));

        PropertyFileMustachifierMojo mojo = getMojo();
        mojo.setMode(Mode.placeholders);
        mojo.setFileset(fs);
        mojo.execute();

        Properties properties = getPropertiesFromFile(targetFile);
        assertEquals(2, properties.size());
        assertEquals("{{param2}}", properties.get("param2"));
        assertEquals("{{param1}}", properties.get("param1"));
    }

    @Test
    public void testSimpleConfiguration() throws Exception {
        final File targetFile = new File("./target/test-classes/mustache/simple/config.properties");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getParent());
        fs.setIncludes(Collections.singletonList(targetFile.getName()));

        PropertyFileMustachifierMojo mojo = getMojo();
        mojo.setMode(Mode.placeholders);
        mojo.setFileset(fs);
        mojo.execute();

        Properties properties = getPropertiesFromFile(targetFile);
        assertEquals(2, properties.size());
        assertEquals("{{param2}}", properties.get("param2"));
        assertEquals("{{param1}}", properties.get("param1"));
    }

    @Test
    public void testSimpleConfigurationWithoutGeneratePlaceholders() throws Exception {
        final File targetFile = new File("./target/test-classes/mustache/simple/config.properties");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getParent());
        fs.setIncludes(Collections.singletonList(targetFile.getName()));

        PropertyFileMustachifierMojo mojo = getMojo();
        mojo.setMode(Mode.placeholders);
        mojo.setGeneratePlaceholder(false);
        mojo.setFileset(fs);
        mojo.execute();

        Properties properties = getPropertiesFromFile(targetFile);
        assertEquals(2, properties.size());
        assertEquals("34", properties.get("param2"));
        assertEquals("12", properties.get("param1"));
    }

    @Test
    public void testMultipleFiles() throws Exception {
        final File targetFile1 = new File("./target/test-classes/mustache/sub1/config.properties");
        final File targetFile2 = new File("./target/test-classes/mustache/sub1/sub2/configsub.properties");
        final FileSet fs = new FileSet();
        fs.setDirectory("./target/test-classes/mustache/sub1");
        fs.setIncludes(Collections.singletonList("**/*.properties"));

        PropertyFileMustachifierMojo mojo = getMojo();
        mojo.setMode(Mode.placeholders);
        mojo.setFileset(fs);
        mojo.execute();

        Properties properties1 = getPropertiesFromFile(targetFile1);
        assertEquals(2, properties1.size());
        assertEquals("{{param2}}", properties1.get("param2"));
        assertEquals("{{param1}}", properties1.get("param1"));

        Properties properties2 = getPropertiesFromFile(targetFile2);
        assertEquals(2, properties2.size());
        assertEquals("{{paramsub2}}", properties2.get("paramsub2"));
        assertEquals("{{paramsub1}}", properties2.get("paramsub1"));
    }

    @Test
    public void testSimpleConfigurationWithDelimiters() throws Exception {
        final File targetFile = new File("./target/test-classes/mustache/config.properties");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getParent());
        fs.setIncludes(Collections.singletonList(targetFile.getName()));

        PropertyFileMustachifierMojo mojo = getMojo();
        mojo.setMode(Mode.placeholders);
        mojo.setFileset(fs);
        mojo.setDelimiters("%% ]]");
        mojo.execute();

        Properties properties = getPropertiesFromFile(targetFile);
        assertEquals(2, properties.size());
        assertEquals("%%param2]]", properties.get("param2"));
        assertEquals("%%param1]]", properties.get("param1"));
    }


    @Test(expected = MojoExecutionException.class)
    public void testWrogConfigurationWithDelimiters() throws Exception {
        final File targetFile = new File("./target/test-classes/mustache/config.properties");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getParent());
        fs.setIncludes(Collections.singletonList(targetFile.getName()));

        PropertyFileMustachifierMojo mojo = getMojo();
        mojo.setMode(Mode.placeholders);
        mojo.setFileset(fs);
        mojo.setDelimiters("%%]]");
        mojo.execute();
    }

    @Test
    public void testSimpleConfigurationWithPlaceholdersInValues() throws Exception {
        final File targetFile = new File("./target/test-classes/mustache/withplaceholders/config.properties");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getParent());
        fs.setIncludes(Collections.singletonList(targetFile.getName()));

        PropertyFileMustachifierMojo mojo = getMojo();
        mojo.setMode(Mode.placeholders);
        mojo.setFileset(fs);
        mojo.setValueSeparator(":");
        mojo.execute();

        Properties properties = getPropertiesFromFile(targetFile);
        assertEquals(4, properties.size());
        assertEquals("{{param22}}", properties.get("param2"));
        assertEquals("{{PARAM1}}", properties.get("param1"));
        assertEquals("{{param3}}", properties.get("param3"));
        assertEquals("http://{{host}}:{{port}}/{{context}}", properties.get("param4"));
    }

    @Test
    public void testGenerateSimpleConfiguration() throws Exception {
        final File targetFile = new File("./target/test-classes/generate/simple/config.properties");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getParent());
        fs.setIncludes(Collections.singletonList(targetFile.getName()));

        PropertyFileMustachifierMojo mojo = getMojo();
        mojo.setMode(Mode.values);
        mojo.setFileset(fs);
        mojo.execute();

        Properties properties = getPropertiesFromFile(targetFile);
        assertEquals(2, properties.size());
        assertEquals("34", properties.get("param2"));
        assertEquals("12", properties.get("param1"));
    }


    @Test
    public void testGenerateSimpleConfigurationWithPlaceholdersInValues() throws Exception {
        final File targetFile = new File("./target/test-classes/generate/withplaceholders/config.properties");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getParent());
        fs.setIncludes(Collections.singletonList(targetFile.getName()));

        PropertyFileMustachifierMojo mojo = getMojo();
        mojo.setMode(Mode.values);
        mojo.setFileset(fs);
        mojo.setValueSeparator(":");
        mojo.execute();

        Properties properties = getPropertiesFromFile(targetFile);
        assertEquals(4, properties.size());
        assertEquals("{{param22}}", properties.get("param2"));
        assertEquals("45", properties.get("param1"));
        assertEquals("44", properties.get("param3"));
        assertEquals("http://localhost:8080/default", properties.get("param4"));
    }


    private PropertyFileMustachifierMojo getMojo() throws Exception {
        final String basedir = getBasedir();
        FileUtils.copyDirectoryStructure(new File(basedir, "src/test/resources/mustache"), new File(basedir, "target/test-classes/mustache"));

        final File pomFile = getTestFile("target/test-classes/mustache/pom.xml");
        assertNotNull(pomFile);
        assertTrue(pomFile.exists());

        PropertyFileMustachifierMojo propertyFileMustachifierMojo = (PropertyFileMustachifierMojo) lookupMojo("mustache", pomFile);
        assertNotNull(propertyFileMustachifierMojo);
        return propertyFileMustachifierMojo;
    }

    private static Properties getPropertiesFromFile(final File targetFile) throws IOException {
        Properties properties = new Properties();
        properties.load(Files.newReader(targetFile, Charset.defaultCharset()));
        return properties;
    }


}
