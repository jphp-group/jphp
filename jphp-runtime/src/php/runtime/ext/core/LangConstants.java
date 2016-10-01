package php.runtime.ext.core;


import php.runtime.Startup;
import php.runtime.common.StringUtils;
import php.runtime.ext.support.compile.ConstantsContainer;

import java.util.Arrays;

public class LangConstants extends ConstantsContainer {
    public final static int PHP_INT_SIZE = 8;

    public final static int DEBUG_BACKTRACE_PROVIDE_OBJECT = 1;
    public final static int DEBUG_BACKTRACE_IGNORE_ARGS = 2;

    public final static int EXTR_OVERWRITE  = 0;
    public final static int EXTR_IF_EXISTS  = 6;
    public final static int EXTR_PREFIX_ALL = 3;
    public final static int EXTR_PREFIX_IF_EXISTS = 5;
    public final static int EXTR_PREFIX_INVALID = 4;
    public final static int EXTR_PREFIX_SAME = 2;
    public final static int EXTR_REFS = 256;
    public final static int EXTR_SKIP = 1;

    public static final String PHP_OS;

    static {
        String[] strings = System.getProperty("os.name").split(" ");

        if (strings.length > 1) {
            PHP_OS = StringUtils.join(Arrays.copyOf(strings, strings.length - 1), " ");
        } else {
            PHP_OS = strings[0];
        }
    }
}
