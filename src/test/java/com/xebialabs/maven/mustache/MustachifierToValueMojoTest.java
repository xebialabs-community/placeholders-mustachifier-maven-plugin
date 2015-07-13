package com.xebialabs.maven.mustache;

import java.io.File;
import java.util.Collections;
import java.util.Properties;
import org.apache.maven.shared.model.fileset.FileSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class MustachifierToValueMojoTest extends MustachifierMojoTest {

    @Test
    public void testSimpleConfigurationWithPlaceholdersInValues() throws Exception {
        final File targetFile = new File("./target/test-classes/mustache/withplaceholders/config.properties");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getParent());
        fs.setIncludes(Collections.singletonList(targetFile.getName()));

        MustachifierToValueMojo mojo = getMojo();
        mojo.setFilesets(Collections.singletonList(fs));
        mojo.setValueSeparator(":");
        mojo.execute();

        Properties properties = getPropertiesFromFile(targetFile);
        assertProperties(properties);
    }

    @Test
    public void testSimpleConfigurationWithSeparator() throws Exception {
        final File targetFile = new File("./target/test-classes/mustache/separator/config.properties");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getParent());
        fs.setIncludes(Collections.singletonList(targetFile.getName()));

        MustachifierToValueMojo mojo = getMojo();
        mojo.setFilesets(Collections.singletonList(fs));
        mojo.setValueSeparator("%");
        mojo.execute();

        Properties properties = getPropertiesFromFile(targetFile);
        assertProperties(properties);
    }


    @Test
    public void testSimpleConfigurationWithDelimiters() throws Exception {
        final File targetFile = new File("./target/test-classes/mustache/delimiters/config.properties");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getParent());
        fs.setIncludes(Collections.singletonList(targetFile.getName()));

        MustachifierToValueMojo mojo = getMojo();
        mojo.setFilesets(Collections.singletonList(fs));
        mojo.setValueSeparator(":");
        mojo.setDelimiters("%% %%");
        mojo.execute();

        Properties properties = getPropertiesFromFile(targetFile);
        assertEquals(4, properties.size());
        assertEquals("45", properties.get("param1"));
        assertEquals("%%param22%%", properties.get("param2"));
        assertEquals("45", properties.get("param3"));
        assertEquals("http://localhost:8080/default", properties.get("param4"));
    }


    @Test
    public void testWithArchive() throws Exception {
        final File targetFile = new File("./target/test-classes/archives/myjar.jar");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getAbsolutePath());
        fs.setIncludes(Collections.singletonList("**/*.properties"));

        MustachifierToValueMojo mojo = getMojo();
        mojo.setFilesets(Collections.singletonList(fs));
        mojo.setValueSeparator(":");
        mojo.execute();

        final Properties properties = getPropertiesFromFile(targetFile, "withplaceholders/config.properties", true);
        assertPropertiesInJar(properties);
    }

    @Test
    public void testWithArchiveWithoutManifest() throws Exception {
        final File targetFile = new File("./target/test-classes/archives/withoutmanifest.jar");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getAbsolutePath());
        fs.setIncludes(Collections.singletonList("config.properties"));

        MustachifierToValueMojo mojo = getMojo();
        mojo.setFilesets(Collections.singletonList(fs));
        mojo.setValueSeparator(":");
        mojo.execute();

        final Properties properties = getPropertiesFromFile(targetFile, "config.properties", false);
        assertPropertiesInJar(properties);
    }

    private void assertPropertiesInJar(final Properties properties) {
        assertEquals(4, properties.size());
        assertEquals("45", properties.get("param1"));
        assertEquals("{{param22}}", properties.get("param2"));
        assertEquals("45", properties.get("param3"));
        assertEquals("http://localhost:8080/default", properties.get("param4"));
    }

    final static void assertProperties(final Properties properties) {
        assertEquals(6, properties.size());
        assertEquals("45", properties.get("param1"));
        assertEquals("{{param22}}", properties.get("param2"));
        assertEquals("45", properties.get("param3"));
        assertEquals("http://localhost:8080/default", properties.get("param4"));
        assertEquals("45%foo", properties.get("param5"));
        assertEquals("", properties.get("param6"));
    }


    private MustachifierToValueMojo getMojo() throws Exception {
        return (MustachifierToValueMojo) getMojo("target/test-classes/mustache/pom-tovalue.xml", "mustache-tovalue");
    }


}
