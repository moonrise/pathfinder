package com.pathfind.config;

import com.pathfind.utils.args.Argument;
import com.pathfind.utils.args.Config;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bjeong
 * Date: Oct 7, 2010
 * Time: 2:43:45 PM
 */
public class MyConfig extends Config {
    // create one static Argument instance for each parameter to be parsed from the command line
    public static final Argument isPackage = new Argument("-p", true, "search package segments, not classes");
    public static final Argument searchRoot = new Argument("-d", false, "directory where search should start from", ".");
    public static final Argument isVerbose = new Argument("-v", true, "verbose output");

    // add the argument above to form a collection - this will be passed on to the super via getArguments()
    private static final Argument[] arguments = {searchRoot, isPackage, isVerbose};

    // the private singleton instance that is not to be garbage collected.
    private static MyConfig globalAndPrivateConfigAccessedByStaticInstance;


    // each construction sets the static singleton
    public MyConfig(String[] args) {
        super(args);
        globalAndPrivateConfigAccessedByStaticInstance = this;
    }

    @Override
    public List<Argument> getArguments() {
        return Arrays.asList(arguments);
    }
}
