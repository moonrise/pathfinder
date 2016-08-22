package com.pathfind.utils.args;

/**
 * Created by IntelliJ IDEA.
 * User: bjeong
 * Date: Oct 7, 2010
 * Time: 10:59:47 AM
 */
public class Argument {
    private String parameterSignature;
    private boolean booleanSignature;
    private String stringValue;
    private String defaultValue;
    private String description;

    public Argument(String parameterSignature, boolean booleanSignature, String description) {
        this(parameterSignature, booleanSignature, description, null);
    }

    public Argument(String parameterSignature, boolean booleanSignature, String description, String defaultValue) {
        this.parameterSignature = parameterSignature;
        this.booleanSignature = booleanSignature;   // presence of the argument says "true"
        this.description = description;
        this.defaultValue = defaultValue;
    }

    public boolean isParameterSignature(String arg) {
        return parameterSignature.equals(arg);
    }

    public boolean processParameterSignature(String arg) {
        if (isParameterSignature(arg) == false) {
            return false;
        }

        if (booleanSignature) {
            stringValue = "true";
        }

        return true;
    }

    public void parseString(String value) {
        stringValue = value;
    }

    public String getParameterSignature() {
        return parameterSignature;
    }

    public boolean isBooleanSignature() {
        return this.booleanSignature;
    }

    public String getStringValue() {
        if (stringValue == null && defaultValue != null) {
            return defaultValue;
        }
        return stringValue;
    }

    public boolean getBooleanValue() {
        return new Boolean(stringValue);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)%s : %s", description, parameterSignature, getDefaultValueInfo(), nullMask(stringValue));
    }

    private String getDefaultValueInfo() {
        if (defaultValue != null) {
            return "; defaults to " + defaultValue;
        }
        return "";
    }

    static public String nullMask(String value) {
        return value == null ? "not provided" : value;
    }
}
