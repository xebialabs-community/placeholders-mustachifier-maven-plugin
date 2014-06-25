package com.xebialabs.maven.mustache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.After;
import org.junit.Before;
import com.google.common.io.Closeables;
import com.google.common.io.Files;

import static org.codehaus.plexus.util.FileUtils.copyDirectoryStructure;


public abstract class MustachifierMojoTest extends AbstractMojoTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    protected static Properties getPropertiesFromFile(final File targetFile) throws IOException {
        Properties properties = new Properties();
        properties.load(Files.newReader(targetFile, Charset.defaultCharset()));
        return properties;
    }

    protected static Properties getPropertiesFromFile(final File archiveFile, String entry, boolean assertOnManifestFile) throws IOException {
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

    protected AbstractMustachifierMojo getMojo(String testFile, String mojoName) throws Exception {
        final String basedir = getBasedir();
        copyDirectoryStructure(new File(basedir, "src/test/resources/mustache"), new File(basedir, "target/test-classes/mustache"));
        copyDirectoryStructure(new File(basedir, "src/test/resources/archives"), new File(basedir, "target/test-classes/archives"));

        final File pomFile = getTestFile(testFile);
        assertNotNull(pomFile);
        assertTrue(pomFile.exists());

        AbstractMustachifierMojo mojo = (AbstractMustachifierMojo) lookupMojo(mojoName, pomFile);
        assertNotNull(mojo);
        return mojo;

    }
}
