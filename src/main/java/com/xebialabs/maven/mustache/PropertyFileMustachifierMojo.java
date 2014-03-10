package com.xebialabs.maven.mustache;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.shared.model.fileset.FileSet;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;

import com.xebialabs.maven.mustache.utils.SkipCommentsBufferedWriter;
import com.xebialabs.maven.mustache.utils.TFiles;

import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.fs.FsSyncException;

import static com.google.common.collect.Maps.fromProperties;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.transformEntries;


/**
 * Mustachify a Java property file.
 *
 * @author Benoit Moussaud
 * @goal mustache
 * @phase compile
 * @threadSafe
 */
public class PropertyFileMustachifierMojo extends AbstractMojo {

    /**
     * FileSet including special entries
     *
     * @parameter
     */
    protected List<FileSet> filesets;

    /**
     * The delimiter format
     *
     * @parameter default-value="{{ }}"
     */
    protected String delimiters;

    /**
     * The separator format from the placeholder name and the default value
     *
     * @parameter default-value=":"
     */
    protected String valueSeparator;

    /**
     * The separator format from the placeholder name and the default value
     *
     * @parameter default-value="placeholders"
     */
    protected Mode mode;


    /**
     * Configure the mojo to generate a default placeholder value from the key name (mode=placeholders only)
     *
     * @parameter default-value="true"
     */
    protected boolean generatePlaceholder;


    public List<FileSet> getFilesets() {
        return filesets;
    }

    public void setFilesets(final List<FileSet> filesets) {
        this.filesets = filesets;
    }

    public String getDelimiters() {
        return delimiters;
    }

    public void setDelimiters(final String delimiters) {
        this.delimiters = delimiters;
    }

    public String getValueSeparator() {
        return valueSeparator;
    }

    public void setValueSeparator(final String valueSeparator) {
        this.valueSeparator = valueSeparator;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(final Mode mode) {
        this.mode = mode;
    }


    public boolean isGeneratePlaceholder() {
        return generatePlaceholder;
    }

    public void setGeneratePlaceholder(final boolean generatePlaceholder) {
        this.generatePlaceholder = generatePlaceholder;
    }

    public void execute() throws MojoExecutionException {

        final Maps.EntryTransformer<String, String, String> transformer = getTransformer();

        ArchiveFileSetManager fileSetManager = new ArchiveFileSetManager();

        for (FileSet fileset : filesets) {
            final String[] includedFiles = fileSetManager.getIncludedFiles(fileset);
            if (includedFiles.length > 0)
                updateManifestFile(fileset.getDirectory());

            for (String includedFile : includedFiles) {
                final Properties properties = new Properties();

                TFile file = new TFile(fileset.getDirectory(), includedFile);

                getLog().debug(" processing " + file);
                load(properties, file, fileset.getModelEncoding());

                Map<String, String> processedEntries = newHashMap(transformEntries(fromProperties(properties), transformer));
                properties.putAll(processedEntries);

                save(properties, file, fileset.getModelEncoding());
            }
            flush(fileset.getDirectory());
        }
    }

    private void flush(final String directory) {
        TFile archive = new TFile(directory);
        if (!archive.isArchive())
            return;

        try {
            TFiles.flush(archive);
        } catch (FsSyncException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private void updateManifestFile(final String directory) throws MojoExecutionException {
        TFile archive = new TFile(directory);
        if (!archive.isArchive()) {
            return;
        }

        getLog().debug("Update the manifest file " + directory);
        TFile manifest = new TFile(archive, "META-INF/MANIFEST.MF");
        if (!manifest.exists()) {
            return;
        }

        try {
            final byte[] bytes;
            bytes = TFiles.toByteArray(manifest);
            TFiles.fromByteArray(manifest, bytes);
        } catch (IOException e) {
            throw new MojoExecutionException("Cannot update manifest file", e);
        }
    }

    protected void save(final Properties properties, final TFile file, String encoding) throws MojoExecutionException {
        Writer out = null;
        try {
            out = TFiles.newWriter(file, Charset.forName(encoding));
            final Writer bufferedWriter = new SkipCommentsBufferedWriter(out);
            properties.store(bufferedWriter, null);
        } catch (Exception e) {
            throw new MojoExecutionException("Cannot write into input file [" + file + "]", e);
        } finally {
            Closeables.closeQuietly(out);
        }
    }

    protected void load(final Properties properties, final TFile file, String encoding) throws MojoExecutionException {
        Reader in = null;
        try {
            in = TFiles.newReader(file, Charset.forName(encoding));
            properties.load(in);
        } catch (IOException e) {
            throw new MojoExecutionException("Cannot read the input file [" + file + "]", e);
        } finally {
            Closeables.closeQuietly(in);
        }
    }

    protected Maps.EntryTransformer<String, String, String> getTransformer() throws MojoExecutionException {
        if (!delimiters.contains(" "))
            throw new MojoExecutionException("Invalid delimiter format: it should include one space character");

        final String beforeDelimiter = delimiters.split(" ")[0];
        final String afterDelimiter = delimiters.split(" ")[1];

        switch (mode) {
            case placeholders:
                return new MustachifierTransformer(beforeDelimiter, afterDelimiter, valueSeparator, generatePlaceholder);
            case values:
                return new ValueSetterTransformer(beforeDelimiter, afterDelimiter, valueSeparator);
        }
        throw new MojoExecutionException("Invalid [" + mode + "]");
    }


}
