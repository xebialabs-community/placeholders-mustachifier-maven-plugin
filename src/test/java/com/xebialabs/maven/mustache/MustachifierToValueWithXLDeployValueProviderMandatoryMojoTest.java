package com.xebialabs.maven.mustache;

import java.io.File;
import java.util.Collections;
import java.util.Properties;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.shared.model.fileset.FileSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.xebialabs.restito.server.StubServer;
import com.xebialabs.restito.support.junit.NeedsServer;
import com.xebialabs.restito.support.junit.ServerDependencyRule;

@RunWith(BlockJUnit4ClassRunner.class)
public class MustachifierToValueWithXLDeployValueProviderMandatoryMojoTest extends MustachifierMojoTest {

    @Rule
    public ServerDependencyRule serverDependencyRule = new ServerDependencyRule();

    protected StubServer stubServer;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        if (serverDependencyRule.isServerDependent()) {
            stubServer = new StubServer(new XLDeployStandardBehavior());
            stubServer.run();
        }
    }

    @After
    public void tearDown() throws Exception {
        if (stubServer != null) {
            stubServer.stop();
        }
        super.tearDown();
    }

    @Test(expected = MojoFailureException.class)
    @NeedsServer
    public void testSimpleConfigurationWithPlaceholdersInValues() throws Exception {
        final File targetFile = new File("./target/test-classes/mustache/withplaceholders/config2.properties");
        final FileSet fs = new FileSet();
        fs.setDirectory(targetFile.getParent());
        fs.setIncludes(Collections.singletonList(targetFile.getName()));

        MustachifierToValueMojo mojo = getMojo();
        mojo.setFilesets(Collections.singletonList(fs));
        mojo.setValueSeparator(":");
        mojo.getValueProvider().setFile(null);
        mojo.execute();
    }

    private MustachifierToValueMojo getMojo() throws Exception {
        return (MustachifierToValueMojo) getMojo("target/test-classes/mustache/pom-tovalue-with-value-provider-mandatory.xml", "mustache-tovalue");
    }
}
