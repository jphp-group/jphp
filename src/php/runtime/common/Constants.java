package php.runtime.common;


import java.io.File;

final public class Constants {

    public final static String OS_NAME = System.getProperty("os.name").toUpperCase();

    public final static boolean OS_WINDOWS = OS_NAME.contains("WIN");
    public final static boolean OS_UNIX = OS_NAME.contains("NIX") || OS_NAME.contains("NUX") || OS_NAME.contains("AIX");
    public final static boolean OS_MAC = OS_NAME.contains("MAC");
    public final static boolean OS_SOLARIS = OS_NAME.contains("SUNOS");

    public final static boolean PATH_NAME_CASE_INSENSITIVE = OS_WINDOWS;

    public final static String DIRECTORY_SEPARATOR = File.separator;
    public final static String PATH_SEPARATOR = File.pathSeparator;

    private Constants(){}
}
