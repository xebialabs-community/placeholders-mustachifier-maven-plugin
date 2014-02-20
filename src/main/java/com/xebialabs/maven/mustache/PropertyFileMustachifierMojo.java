package com.xebialabs.maven.mustache;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

import com.xebialabs.maven.mustache.utils.SkipCommentsBufferedWriter;

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
     * @parameter fileset="mustache.fileset"
     */
    protected FileSet fileset;

    /**
     * The delimiter format
     *
     * @parameter property="${mustache.delimiters}" default-value="{{ }}"
     */
    protected String delimiters;

    /**
     * The separator format from the placeholder name and the default value
     *
     * @parameter property="${mustache.separator}" default-value=":"
     */
    protected String valueSeparator;

    /**
     * The separator format from the placeholder name and the default value
     *
     * @parameter property="${mustache.mode}" default-value="placeholders"
     */
    protected Mode mode;


    /**
     * Configure the mojo to generate a default placeholder value from the key name (mode=placeholders only)
     * @parameter property="${mustache.generateplaceholder}" default-value="true"
     */
    protected boolean generatePlaceholder;


    public FileSet getFileset() {
        return fileset;
    }

    public void setFileset(final FileSet fileset) {
        this.fileset = fileset;
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

        FileSetManager fileSetManager = new FileSetManager(getLog());
        final String[] includedFiles = fileSetManager.getIncludedFiles(fileset);

        for (String includedFile : includedFiles) {
            final Properties properties = new Properties();

            File file = new File(fileset.getDirectory(), includedFile);
            getLog().debug(" processing " + file);
            load(properties, file);

            Map<String, String> processedEntries = newHashMap(transformEntries(fromProperties(properties), transformer));
            properties.putAll(processedEntries);

            save(properties, file);
        }

    }


    protected void save(final Properties properties, final File file) throws MojoExecutionException {
        try {
            final Writer bufferedWriter = new SkipCommentsBufferedWriter(Files.newWriter(file, Charset.forName(fileset.getModelEncoding())));
            properties.store(bufferedWriter, null);
        } catch (Exception e) {
            throw new MojoExecutionException("Cannot write into input file [" + file + "]", e);
        }
    }

    protected void load(final Properties properties, final File file) throws MojoExecutionException {
        try {
            properties.load(Files.newReader(file, Charset.forName(fileset.getModelEncoding())));
        } catch (IOException e) {
            throw new MojoExecutionException("Cannot read the input file [" + file + "]", e);
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
