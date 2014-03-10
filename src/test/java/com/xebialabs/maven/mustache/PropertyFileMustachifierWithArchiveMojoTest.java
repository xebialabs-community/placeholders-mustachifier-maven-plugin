package com.xebialabs.maven.mustache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.shared.model.fileset.FileSet;
import org.codehaus.plexus.util.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import com.google.common.io.Closeables;

@RunWith(BlockJUnit4ClassRunner.class)
public class PropertyFileMustachifierWithArchiveMojoTest extends AbstractMojoTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }


    @Test
    public void testWithArchive() throws Exception {
        final File targetFile = new File("./target/test-classes/archives/myjar.jar");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getAbsolutePath());
        fs.setIncludes(Collections.singletonList("config.properties"));

        PropertyFileMustachifierMojo mojo = getMojo();
        mojo.setMode(Mode.placeholders);
        mojo.setFilesets(Collections.singletonList(fs));
        mojo.setValueSeparator(":");
        mojo.execute();

        final Properties properties = getPropertiesFromFile(targetFile, "config.properties", true);
        assertEquals(2, properties.size());
        assertEquals("{{param2}}", properties.get("param2"));
        assertEquals("{{param1}}", properties.get("param1"));
    }


    @Test
    public void testWithArchiveSubProperties() throws Exception {
        final File targetFile = new File("./target/test-classes/archives/myjar.jar");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getAbsolutePath());
        fs.setIncludes(Collections.singletonList("**/*.properties"));

        PropertyFileMustachifierMojo mojo = getMojo();
        mojo.setMode(Mode.placeholders);
        mojo.setFilesets(Collections.singletonList(fs));
        mojo.setValueSeparator(":");
        mojo.execute();

        Properties properties = getPropertiesFromFile(targetFile, "config.properties", true);
        assertEquals(2, properties.size());
        assertEquals("{{param2}}", properties.get("param2"));
        assertEquals("{{param1}}", properties.get("param1"));

        properties = getPropertiesFromFile(targetFile, "sub1/sub2/configsub.properties", true);
        assertEquals(2, properties.size());
        assertEquals("{{paramsub2}}", properties.get("paramsub2"));
        assertEquals("{{paramsub1}}", properties.get("paramsub1"));
    }

    @Test
    public void testWithArchiveWithoutManifest() throws Exception {
        final File targetFile = new File("./target/test-classes/archives/withoutmanifest.jar");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getAbsolutePath());
        fs.setIncludes(Collections.singletonList("config.properties"));

        PropertyFileMustachifierMojo mojo = getMojo();
        mojo.setMode(Mode.placeholders);
        mojo.setFilesets(Collections.singletonList(fs));
        mojo.setValueSeparator(":");
        mojo.execute();

        final Properties properties = getPropertiesFromFile(targetFile, "config.properties", false);
        assertEquals(2, properties.size());
        assertEquals("{{param2}}", properties.get("param2"));
        assertEquals("{{param1}}", properties.get("param1"));
    }


    private PropertyFileMustachifierMojo getMojo() throws Exception {
        final String basedir = getBasedir();
        FileUtils.copyDirectoryStructure(new File(basedir, "src/test/resources/archives"), new File(basedir, "target/test-classes/archives"));

        final File pomFile = getTestFile("target/test-classes/mustache/pom.xml");
        assertNotNull(pomFile);
        assertTrue(pomFile.exists());

        PropertyFileMustachifierMojo propertyFileMustachifierMojo = (PropertyFileMustachifierMojo) lookupMojo("mustache", pomFile);
        assertNotNull(propertyFileMustachifierMojo);
        return propertyFileMustachifierMojo;
    }

    private static Properties getPropertiesFromFile(final File archiveFile, String entry, boolean assertOnManifestFile) throws IOException {
        Properties properties = new Properties();
        JarInputStream jis = new JarInputStream(new FileInputStream(archiveFile));
        try {
            if (assertOnManifestFile) {
                final Manifest manifest = jis.getManifest();
                assertNotNull(manifest);
            }

            JarEntry jarentry;
            while ((jarentry = jis.getNextJarEntry()) != null) {
                if (entry.equals(jarentry.getName())) {
                    properties.load(jis);
                    return properties;
                }
            }
        } finally {

            Closeables.closeQuietly(jis);
        }
        throw new RuntimeException("Entry " + entry + " not found in " + archiveFile);
    }
}
