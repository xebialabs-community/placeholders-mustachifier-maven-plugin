package com.xebialabs.maven.mustache;

import java.io.File;
import java.util.Collections;
import java.util.Properties;
import org.apache.maven.shared.model.fileset.FileSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class MustachifierToValueWithPropertyFileValueProviderMojoTest extends MustachifierMojoTest {

    @Test
    public void testSimpleConfigurationWithPlaceholdersInValues() throws Exception {
        final File targetFile = new File("./target/test-classes/mustache/withplaceholders/config2.properties");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getParent());
        fs.setIncludes(Collections.singletonList(targetFile.getName()));

        MustachifierToValueMojo mojo = getMojo();
        mojo.setFilesets(Collections.singletonList(fs));
        mojo.setValueSeparator(":");
        mojo.getValueProvider().getFile().setPath("target/test-classes/provider/values.properties");
        mojo.getValueProvider().setXldeploy(null);
        mojo.execute();

        Properties properties = getPropertiesFromFile(targetFile);
        assertProperties(properties);
    }

    private void assertProperties(final Properties properties) {
        assertEquals(5, properties.size());
        assertEquals("45", properties.get("param1"));
        assertEquals("{{param22}}", properties.get("param2"));
        assertEquals("45", properties.get("param3"));
        assertEquals("http://www.xebialabs.com:9090/xldeploy", properties.get("param4"));
        assertEquals("benoit", properties.get("param5"));
    }

    private MustachifierToValueMojo getMojo() throws Exception {
        return (MustachifierToValueMojo) getMojo("target/test-classes/mustache/pom-tovalue-with-value-provider.xml", "mustache-tovalue");
    }
}
