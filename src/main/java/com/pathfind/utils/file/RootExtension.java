package com.pathfind.utils.file;

import java.io.File;


/**
 * returns file root name and extension name for the given file entity
 *
 * Created by IntelliJ IDEA.
 * User: bjeong
 * Date: Oct 11, 2010
 * Time: 4:58:50 PM
 */
public class RootExtension {
    public String root;
    public String extension;

    public RootExtension(File file) {
        this(file.getName());
    }

    /**
     * @param fileName is the root name delimited by a period.
     */
    public RootExtension(String fileName) {
        int periodIndex = fileName.lastIndexOf(".");
        if (periodIndex < 0) {      // no extension; only the root
            root = fileName;
            return;
        }

        if (periodIndex == 0) {     // no root, but extension only like ".svn"
            extension = fileName.substring(1);
            return;
        }

        // not we have both
        root = fileName.substring(0, periodIndex);
        extension = fileName.substring(periodIndex + 1);    // assuming no file name can end with a period!
    }
}
