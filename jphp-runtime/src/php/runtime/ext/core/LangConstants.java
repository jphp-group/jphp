package php.runtime.ext.core;


import php.runtime.Information;
import php.runtime.Memory;
import php.runtime.Startup;
import php.runtime.common.StringUtils;
import php.runtime.ext.support.compile.ConstantsContainer;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.StringMemory;

import java.util.Arrays;

public class LangConstants extends ConstantsContainer {
    public final static Memory PHP_VERSION = StringMemory.valueOf(Information.LIKE_PHP_VERSION);
    public final static Memory PHP_MAJOR_VERSION = LongMemory.valueOf(5);
    public final static Memory PHP_MINOR_VERSION = LongMemory.valueOf(6);
    public final static Memory PHP_RELEASE_VERSION = LongMemory.valueOf(99);
    public final static Memory PHP_MAXPATHLEN = LongMemory.valueOf(4096);
    public final static Memory PHP_EOL = StringMemory.valueOf(System.lineSeparator());

    public final static Memory PHP_INT_SIZE = LongMemory.valueOf(8);
    public final static Memory PHP_INT_MAX = LongMemory.valueOf(Long.MAX_VALUE);
    public final static Memory PHP_INT_MIN = LongMemory.valueOf(Long.MIN_VALUE);

    public final static Memory PHP_FLOAT_MAX = DoubleMemory.valueOf(Double.MAX_VALUE);
    public final static Memory PHP_FLOAT_MIN = DoubleMemory.valueOf(Double.MIN_VALUE);

    public final static Memory DEBUG_BACKTRACE_PROVIDE_OBJECT = LongMemory.valueOf(1);
    public final static Memory DEBUG_BACKTRACE_IGNORE_ARGS = LongMemory.valueOf(2);

    public final static Memory EXTR_OVERWRITE  = LongMemory.valueOf(0);
    public final static Memory EXTR_IF_EXISTS  = LongMemory.valueOf(6);
    public final static Memory EXTR_PREFIX_ALL = LongMemory.valueOf(3);
    public final static Memory EXTR_PREFIX_IF_EXISTS = LongMemory.valueOf(5);
    public final static Memory EXTR_PREFIX_INVALID = LongMemory.valueOf(4);
    public final static Memory EXTR_PREFIX_SAME = LongMemory.valueOf(2);
    public final static Memory EXTR_REFS = LongMemory.valueOf(256);
    public final static Memory EXTR_SKIP = LongMemory.valueOf(1);

    public static final Memory PHP_OS;
    public static final Memory PHP_OS_FAMILY;

    static {
        String[] strings = System.getProperty("os.name").split(" ");

        if (strings.length > 1) {
            PHP_OS = StringMemory.valueOf(
                    StringUtils.join(Arrays.copyOf(strings, strings.length - 1), " ")
            );
        } else {
            PHP_OS = StringMemory.valueOf(
                    strings[0]
            );
        }

        PHP_OS_FAMILY = StringMemory.valueOf(strings[0]);
    }
}
