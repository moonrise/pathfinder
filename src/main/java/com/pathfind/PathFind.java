package com.pathfind;

import com.pathfind.config.MyConfig;
import com.pathfind.scanner.DirectoryScanner;
import com.pathfind.scanner.SearchTarget;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: bjeong
 * Date: Oct 7, 2010
 * Time: 10:23:15 AM
 */
public class PathFind {
    public static void main(String[] args) {
        System.out.println();       // initial delimiting blank line


        // get the command line parameters and form the configuration instance
        MyConfig myConfig = new MyConfig(args);
        myConfig.dump();

        String errorMessage = validateLaunchConfig(myConfig);
        if (errorMessage != null) {
            System.out.println(String.format("Usage error: %s\n", errorMessage));
            System.exit(1);
        }

        // now do it
        try {
            DirectoryScanner scanner = new DirectoryScanner(
                    new File(MyConfig.searchRoot.getStringValue()),
                    new SearchTarget(myConfig.nonParameterValue, myConfig.isPackage.getBooleanValue()));
            scanner.startScan();
            scanner.showSanStat();
        }
        catch (Exception e) {
            System.out.println(e);
        }

        System.out.println();       // terminating delimiter blank line
    }

    /**
     * @param myConfig
     * @return error message with any errors. null return means "all to go"
     */
    private static String validateLaunchConfig(MyConfig myConfig) {
        String searchRootPath = myConfig.searchRoot.getStringValue();
        File searchRoot = new File(searchRootPath);
        if (!searchRoot.exists()) {
            return String.format("Search path '%s' does not exist.", searchRootPath);
        }

        if (!searchRoot.isDirectory()) {
            System.out.printf("Search path '%s' is not a directory.", searchRootPath);
        }

        if (myConfig.nonParameterValue == null) {
            if (myConfig.isPackage.getBooleanValue()) {
                return "Provide a package segments to search (example 'com.foo.bar')";
            }
            else {
                return "Provide a class to search (with no .java or .class extensions)";
            }
        }

        return null;
    }
}
