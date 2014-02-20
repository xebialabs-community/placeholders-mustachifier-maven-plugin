package com.xebialabs.maven.mustache.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public class SkipCommentsBufferedWriter extends BufferedWriter {

    boolean skipFirstNewLine = false;

    public SkipCommentsBufferedWriter(final Writer out) {
        super(out);
    }

    @Override
    public void write(final String str) throws IOException {
        if (str.startsWith("#"))
            return;
        super.write(str);
    }

    @Override
    public void newLine() throws IOException {
        if (skipFirstNewLine) {
            super.newLine();
        } else {
            skipFirstNewLine = true;
        }
    }
}
