package com.xebialabs.maven.mustache;

import java.io.File;
import org.codehaus.plexus.util.DirectoryScanner;

import de.schlichtherle.truezip.file.TFile;

public class ArchiveScanner extends DirectoryScanner {

    @Override
    protected void scandir(File dir, String vpath, boolean fast) {
        String[] newfiles = dir.list();
        for (int i = 0; i < newfiles.length; i++) {
            String name = vpath + newfiles[i];
            File file = new TFile(dir, newfiles[i]);
            if (file.isDirectory()) {
                if (isIncluded(name)) {
                    if (!isExcluded(name)) {
                        if (isSelected(name, file)) {
                            dirsIncluded.addElement(name);
                            if (fast) {
                                scandir(file, name + File.separator, fast);
                            }
                        } else {
                            everythingIncluded = false;
                            dirsDeselected.addElement(name);
                            if (fast && couldHoldIncluded(name)) {
                                scandir(file, name + File.separator, fast);
                            }
                        }

                    } else {
                        everythingIncluded = false;
                        dirsExcluded.addElement(name);
                        if (fast && couldHoldIncluded(name)) {
                            scandir(file, name + File.separator, fast);
                        }
                    }
                } else {
                    everythingIncluded = false;
                    dirsNotIncluded.addElement(name);
                    if (fast && couldHoldIncluded(name)) {
                        scandir(file, name + File.separator, fast);
                    }
                }
                if (!fast) {
                    scandir(file, name + File.separator, fast);
                }
            } else if (file.isFile()) {
                if (isIncluded(name)) {
                    if (!isExcluded(name)) {
                        if (isSelected(name, file)) {
                            filesIncluded.addElement(name);
                        } else {
                            everythingIncluded = false;
                            filesDeselected.addElement(name);
                        }
                    } else {
                        everythingIncluded = false;
                        filesExcluded.addElement(name);
                    }
                } else {
                    everythingIncluded = false;
                    filesNotIncluded.addElement(name);
                }
            }
        }

    }

}
