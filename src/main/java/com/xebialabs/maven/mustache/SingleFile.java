package com.xebialabs.maven.mustache;

/**
 * Created by clalleme on 31/03/2015.
 */
public class SingleFile {
    public static final String UTF_8 = "UTF-8";
    String sourceFile;
    String targetFileName;
    boolean filterOnlyMustacheProperties = false;
    private String modelEncoding = UTF_8;


    public SingleFile() {
    }

    public SingleFile(final String aSourceFile, final String aTargetFileName, final boolean aFilterOnlyMustacheProperties) {
        sourceFile = aSourceFile;
        targetFileName = aTargetFileName;
        filterOnlyMustacheProperties = aFilterOnlyMustacheProperties;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(final String aSourceFile) {
        sourceFile = aSourceFile;
    }

    public String getTargetFileName() {
        return targetFileName;
    }

    public void setTargetFileName(final String aTargetFileName) {
        targetFileName = aTargetFileName;
    }

    public boolean isFilterOnlyMustacheProperties() {
        return filterOnlyMustacheProperties;
    }

    public void setFilterOnlyMustacheProperties(final boolean aFilterOnlyMustacheProperties) {
        filterOnlyMustacheProperties = aFilterOnlyMustacheProperties;
    }

    public String getModelEncoding() {
        return modelEncoding;
    }

    public void setModelEncoding(final String aModelEncoding) {
        modelEncoding = aModelEncoding;
    }
}
