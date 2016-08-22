package com.pathfind.scanner;

import com.pathfind.config.MyConfig;

import java.io.File;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: bjeong
 * Date: Oct 7, 2010
 * Time: 4:46:02 PM
 */
public class DirectoryScanner {
    private File searchRoot;
    private SearchTarget searchTarget;

    private long startTime;
    private long endTime;

    
    public DirectoryScanner(File searchRoot, SearchTarget searchTarget) {
        this.searchRoot = searchRoot;
        this.searchTarget = searchTarget;
    }

    public void startScan() {
        startTime = new Date().getTime();
        crawlDirectory(searchRoot);
        endTime = new Date().getTime();
    }

    private void crawlDirectory(File directory) {
        for (File file : directory.listFiles()) {
            // exclusion
            if (file.isDirectory() && file.getName().startsWith(".")) {
                if (MyConfig.isVerbose.getBooleanValue()) {
                    System.out.printf("Skipping %s ...\n", file.getPath());
                }
                continue;
            }

            searchTarget.onFileVisit(file);

            // recurs
            if (file.isDirectory()) {
                crawlDirectory(file);
            }
        }
    }

    public void showSanStat() {
        long executionTime = endTime - startTime;
        System.out.printf("\n---------------------- scanner summary -----------------------------------------------\n" +
                "Found %,d match(es) of '%s' after %,d visits (files:%,d + archives:%,d) in %tM:%<tS:%<tL.\n",
                searchTarget.matches.size(), searchTarget.searchTarget,
                searchTarget.visitCountFile + searchTarget.visitCountArchive,
                searchTarget.visitCountFile, searchTarget.visitCountArchive, executionTime);

        int n = 0;
        for (String match : searchTarget.matches) {
            SearchTarget.showFileMatch(match, ++n);
        }
    }
}
