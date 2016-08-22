package com.pathfind.utils.args;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bjeong
 * Date: Oct 7, 2010
 * Time: 10:38:17 AM
 */
public abstract class Config {
    public String nonParameterValue;

    public Config(String[] args) {
        parseUserArgs(args);
    }

    public abstract List<Argument> getArguments();

    private void parseUserArgs(String[] args) {
        Argument currentArg = null;

        for (String arg : args) {
            boolean gotIt = false;
            if (currentArg != null) {           // parameter value
                currentArg.parseString(arg);
                currentArg = null;
                gotIt = true;
            }
            else if (arg.indexOf('-') == 0) {    // parameter itself
                for (Argument argument : getArguments()) {
                    if (argument.processParameterSignature(arg)) {
                        if (argument.isBooleanSignature() == false) {
                            currentArg = argument;
                        }
                        gotIt = true;
                        break;
                    }
                }
            }
            else if (arg.indexOf('-') != 0) {                              // non parameter arguments
                nonParameterValue = arg;
                gotIt = true;
            }

            if (gotIt == false) {
                System.err.printf("Argument value '%s' is not understood.\n", arg);
            }
        }

        if (currentArg != null) {
            System.err.printf("Missing value for the parameter %s", currentArg.getParameterSignature());
        }
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("========================== command options ================================================\n");
        for (Argument arg : getArguments()) {
            out.append(arg);
            out.append('\n');
        }
        out.append(String.format("search target (command value - required): %s\n\n", Argument.nullMask(nonParameterValue)));
        out.append("Examples: PathFind                        : search PathFind.java or PathFind.class in the current directory\n");
        out.append("          -d ~/my/path PathFind           : search PathFind class in the given directory\n");
        out.append("          -d ~/my/path -p -v com.foo.bar  : search package segments with verbose flag on\n");
        out.append("===========================================================================================\n");
        return out.toString();
    }

    public void dump() {
        System.out.println(this);
    }
}
