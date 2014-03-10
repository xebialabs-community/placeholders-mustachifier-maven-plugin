package com.xebialabs.maven.mustache;

import java.util.Collections;
import org.apache.maven.shared.model.fileset.FileSet;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ArchiveFileSetManagerTest {


    @Test
    public void testDummy() throws Exception {
        FileSet fs = new FileSet();
        fs.setDirectory("src/test/resources/archives/dummy.jar");
        fs.setIncludes(Collections.singletonList("*.properties"));

        ArchiveFileSetManager scanner = new ArchiveFileSetManager();
        final String[] includedFiles = scanner.getIncludedFiles(fs);
        assertEquals(0, includedFiles.length);
    }

    @Test
    public void testJar() throws Exception {
        FileSet fs = new FileSet();
        fs.setDirectory("src/test/resources/archives/myjar.jar");
        fs.setIncludes(Collections.singletonList("**/*.properties"));

        ArchiveFileSetManager scanner = new ArchiveFileSetManager();
        final String[] includedFiles = scanner.getIncludedFiles(fs);
        assertEquals(7, includedFiles.length);
    }
}
