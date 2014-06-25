package com.xebialabs.maven.mustache;

import java.io.File;
import java.util.Collections;
import java.util.Properties;
import org.apache.maven.shared.model.fileset.FileSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class MustachifierToKeyMojoTest extends MustachifierMojoTest {

    @Test
    public void testSimpleConfigurationWithPlaceholdersInValues() throws Exception {
        final File targetFile = new File("./target/test-classes/mustache/withplaceholders/config.properties");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getParent());
        fs.setIncludes(Collections.singletonList(targetFile.getName()));

        MustachifierToKeyMojo mojo = getMojo();
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

        MustachifierToKeyMojo mojo = getMojo();
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

        MustachifierToKeyMojo mojo = getMojo();
        mojo.setFilesets(Collections.singletonList(fs));
        mojo.setValueSeparator(":");
        mojo.setDelimiters("%% %%");
        mojo.execute();

        Properties properties = getPropertiesFromFile(targetFile);
        assertEquals(4, properties.size());
        assertEquals("%%param22%%", properties.get("param2"));
        assertEquals("%%PARAM1%%", properties.get("param1"));
        assertEquals("45", properties.get("param3"));
        assertEquals("http://%%host%%:%%port%%/%%context%%", properties.get("param4"));
    }

    @Test
    public void testWithArchive() throws Exception {
        final File targetFile = new File("./target/test-classes/archives/myjar.jar");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getAbsolutePath());
        fs.setIncludes(Collections.singletonList("**/withplaceholders/*.properties"));

        MustachifierToKeyMojo mojo = getMojo();
        mojo.setFilesets(Collections.singletonList(fs));
        mojo.setValueSeparator(":");
        mojo.execute();

        final Properties properties = getPropertiesFromFile(targetFile, "withplaceholders/config.properties", true);
        assertProperties(properties);
    }


    @Test
    public void testWithArchiveWithoutManifest() throws Exception {
        final File targetFile = new File("./target/test-classes/archives/withoutmanifest.jar");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getAbsolutePath());
        fs.setIncludes(Collections.singletonList("config.properties"));

        MustachifierToKeyMojo mojo = getMojo();
        mojo.setFilesets(Collections.singletonList(fs));
        mojo.setValueSeparator(":");
        mojo.execute();

        final Properties properties = getPropertiesFromFile(targetFile, "config.properties", false);
        assertProperties(properties);
    }

    private void assertProperties(final Properties properties) {
        assertEquals(4, properties.size());
        assertEquals("{{param22}}", properties.get("param2"));
        assertEquals("{{PARAM1}}", properties.get("param1"));
        assertEquals("45", properties.get("param3"));
        assertEquals("http://{{host}}:{{port}}/{{context}}", properties.get("param4"));
    }


    private MustachifierToKeyMojo getMojo() throws Exception {
        return (MustachifierToKeyMojo) getMojo("target/test-classes/mustache/pom-tokey.xml", "mustache-tokey");
    }


}
