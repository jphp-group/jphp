package php.runtime;

import php.runtime.ext.*;

import java.util.HashMap;
import java.util.Map;

final public class Information {
    public final static String NAMESPACE_SEP = "\\";
    public final static char NAMESPACE_SEP_CHAR = '\\';

    public static final String NAME = "JPHP";
    public static final String CORE_VERSION = "0.4-SNAPSHOT";
    public static final String LIKE_PHP_VERSION = "5.3.2";
    public static final String LIKE_ZEND_VERSION = "2.2";

    public static final String COPYRIGHT = "2013 - 2014, develnext.org, Dmitriy Zayceff aka Dim-S";
    public static final String LICENSE = "Apache License 2.0";


    public static final Map<String, String> EXTENSIONS = new HashMap<String, String>();

    static {
        EXTENSIONS.put("spl", SPLExtension.class.getName());
    }
}
