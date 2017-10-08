package org.develnext.jphp.zend.ext.standard;

import php.runtime.Memory;
import php.runtime.ext.core.*;
import php.runtime.ext.core.LangConstants;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.StringMemory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.TimeZone;

public class LangFunctions extends FunctionsContainer {
    public static String php_uname() {
        return php_uname('a');
    }

    public static String php_uname(char mode) {
        switch (mode) {
            case 's':
                return LangConstants.PHP_OS.toString();
            case 'n':
                try {
                    return InetAddress.getLocalHost().getHostName();
                } catch (UnknownHostException e) {
                    return "localhost";
                }
            case 'v':
            case 'r':
                return System.getProperty("os.version");
            case 'm':
                return System.getProperty("os.arch");
            case 'a':
                return php_uname('s') + " " + php_uname('n') + " " + php_uname('r') + " " + php_uname('v') + " " + php_uname('m');
            default:
                return null;
        }
    }

    public static String php_sapi_name() {
        return "cli";
    }
}
