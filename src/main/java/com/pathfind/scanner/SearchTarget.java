package com.pathfind.scanner;

import com.pathfind.config.MyConfig;
import com.pathfind.exception.PathFinderException;
import com.pathfind.utils.file.RootExtension;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by IntelliJ IDEA.
 * User: bjeong
 * Date: Oct 7, 2010
 * Time: 5:03:29 PM
 */
public class SearchTarget {
    private static final String PACKAGE_SEPARATOR = ".";

    public String searchTarget;
    public boolean isSearchPackage;   // search package path, not the class entity

    private String[] segments;
    private String leafSegment;     // the leaf of segments - for better performance

    public int visitCountFile = 1;      // starts with one to count the initial root folder scan
    public int visitCountArchive = 0;
    public ArrayList<String> matches = new ArrayList<String>();


    /**
     * @param searchTarget can be either
     *                     1. Java class qualified or not qualified with a package path
     *                     or
     *                     2. package path only
     *                     <p/>
     *                     Java class name is a match either in foo.java or foo.class forms.
     *                     Note that '.java' or '.class' extension should be omitted if it meant to be the class name.
     *                     Otherwise, .java or .class will be interpreted as the package segment.
     *                     <p/>
     *                     The search target starting with or ending with '.' is invalid.
     */
    public SearchTarget(String searchTarget, boolean searchPackage) throws PathFinderException {
        this.searchTarget = searchTarget;
        this.isSearchPackage = searchPackage;

        int index = searchTarget.lastIndexOf(PACKAGE_SEPARATOR);

        segments = searchTarget.split(PACKAGE_SEPARATOR.equals(".") ? "\\." : PACKAGE_SEPARATOR);
        if (segments.length > 0) {
            leafSegment = segments[segments.length - 1];
        } else {
            throw new PathFinderException(String.format("Search criteria '%s' is invalid.", searchTarget));
        }
    }

    public void onFileVisit(File file) {
        visitCountFile++;

        showVisitTrace(file.getPath());

        // keep descending if it is an archive file
        if (file.isFile()) {
            RootExtension aFile = new RootExtension(file);
            if ("jar".equalsIgnoreCase(aFile.extension)) {
                DescendToArchive(file);
                return;
            }
            else if (isSearchPackage == false && isMatch(file.getPath(), File.separator)) {
                onMatch(file.getPath());
            }
        }
        else if (isSearchPackage && isMatch(file.getPath(), File.separator)) {
            onMatch(file.getPath());
        }
    }

    private void showVisitTrace(String entry) {
        if (MyConfig.isVerbose.getBooleanValue()) {
            System.out.printf("Visiting %s (%d)\n", entry, visitCountFile + visitCountArchive);
        }
    }

    private void DescendToArchive(File file) {
        JarFile jar;

        try {
            jar = new JarFile(file);
        }
        catch (Exception e) {
            System.err.printf("Error opening archive file: %s\n%s\n", file.getPath(), e.toString());
            //e.printStackTrace();
            return;
        }

        if (MyConfig.isVerbose.getBooleanValue()) {
            System.out.printf("looking into archive file: %s\n", file.getPath());
        }

        for (Enumeration<JarEntry> jarEntries = jar.entries(); jarEntries.hasMoreElements();) {
            JarEntry jarEntry = jarEntries.nextElement();
            if (jarEntry.isDirectory() && isSearchPackage == false) {
                continue;
            }

            String name = jarEntry.getName();
            visitCountArchive++;
            showVisitTrace(String.format("%s : %s", file.getPath(), name));

            if (isMatch(name, "/")) {
                onMatch(String.format("%s : %s", file.getPath(), name));
            }
        }
    }

    private void onMatch(String match) {
        matches.add(match);
        showFileMatch(match, matches.size());
    }

    private boolean isMatch(String path, String pathSeparator) {
        String[] pathSegments = path.split(pathSeparator.equals("\\") ? "\\\\" : pathSeparator);

        if (isSearchPackage == false) {
            RootExtension fileName = new RootExtension(pathSegments[pathSegments.length-1]);
            if (fileName.root == null || fileName.extension == null) {  // sanity check!
                return false;
            }

            String extension = fileName.extension.toLowerCase();        // ignore case for extension
            if (!extension.equals("java") && !extension.equals("class")) {
                return false;
            }

            String root = fileName.root;                                // case sensitive for root name
            if (leafSegment.equals(root) == false) {
                return false;
            }

            if (segments.length == 1) {
                return true;
            }
        }

        // check the path segments
        return isPathSegmentsMatch(pathSegments);
    }

    private boolean isPathSegmentsMatch(String[] pathSegments) {
        int fromPathSegmentIndex = pathSegments.length - (isSearchPackage ? 1 : 2);     // -2 because we exclude the leaf
        int toPathSegmentIndex = isDriveLetter(pathSegments[0]) ? 1 : 0;    // exclude the drive letter
        int fromTargetSegmentIndex = segments.length - (isSearchPackage ? 1 : 2);       // -2 again because we exclude the leaf
        final int toTargetSegmentIndex = 0;

        int lastTargetSegmentIndex = -1;
        for (int i = fromPathSegmentIndex, j = fromTargetSegmentIndex; i >= toPathSegmentIndex && j >= toTargetSegmentIndex; i--, j--) {
            lastTargetSegmentIndex = j;
            if (pathSegments[i].equals(segments[j]) == false) {
                return false;
            }
        }

        return lastTargetSegmentIndex == toTargetSegmentIndex;
    }

    private boolean isDriveLetter(String pathSegment) {
        return (pathSegment.length() == 2 && pathSegment.indexOf(":") == 1);
    }

    public static void showFileMatch(String match, int nth) {
        System.out.printf("match (%d): %s\n", nth, match);
    }
}
