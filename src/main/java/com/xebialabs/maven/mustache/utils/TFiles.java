package com.xebialabs.maven.mustache.utils;

import java.io.*;
import java.nio.charset.Charset;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import com.google.common.io.OutputSupplier;

import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileInputStream;
import de.schlichtherle.truezip.file.TFileOutputStream;
import de.schlichtherle.truezip.fs.FsSyncException;



public class TFiles {

    public static BufferedReader newReader(TFile file, Charset charset)
            throws FileNotFoundException {
        return new BufferedReader(
                new InputStreamReader(new TFileInputStream(file), charset));
    }

    public static BufferedWriter newWriter(TFile file, Charset charset)
            throws FileNotFoundException {
        return new BufferedWriter(
                new OutputStreamWriter(new TFileOutputStream(file), charset));
    }

    public static void flush(File dar) throws FsSyncException {
        TFile.umount(new TFile(dar));
    }


    public static byte[] toByteArray(TFile file) throws IOException {
        final TFileInputStream in = new TFileInputStream(file);
        try {
            return ByteStreams.toByteArray(in);
        } finally {
            Closeables.closeQuietly(in);
        }
    }

    public static String toString(TFile file, Charset charset) throws IOException {
        return new String(toByteArray(file), charset);
    }


    public static void fromByteArray(final TFile file, byte[] data) throws IOException {
        final OutputSupplier<TFileOutputStream> to = new OutputSupplier<TFileOutputStream>() {
            @Override
            public TFileOutputStream getOutput() throws IOException {
                return new TFileOutputStream(file);
            }
        };
        try {
            ByteStreams.write(data, to);
        } finally {
            Closeables.closeQuietly(to.getOutput());
        }
    }

    public static void write(final String content, final TFile file, final Charset charset) throws IOException {
        CharStreams.write(content, newWriterSupplier(file, charset, true));
    }

    private static OutputSupplier<OutputStreamWriter> newWriterSupplier(final TFile file, final Charset charset, final boolean b) {
        return CharStreams.newWriterSupplier(new OutputSupplier<OutputStream>(){
            @Override
            public OutputStream getOutput() throws IOException {
                return new TFileOutputStream(file,b);
            }
        },charset);
    }


}
