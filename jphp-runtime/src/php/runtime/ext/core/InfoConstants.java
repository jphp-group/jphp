package php.runtime.ext.core;

import php.runtime.Information;
import php.runtime.Memory;
import php.runtime.common.Constants;
import php.runtime.ext.support.compile.ConstantsContainer;
import php.runtime.memory.StringMemory;

public class InfoConstants extends ConstantsContainer {
    public static Memory JPHP_VERSION = StringMemory.valueOf(Information.CORE_VERSION);
    public static Memory PATH_SEPARATOR = StringMemory.valueOf(Constants.PATH_SEPARATOR);
    public static Memory DIRECTORY_SEPARATOR = StringMemory.valueOf(Constants.DIRECTORY_SEPARATOR);
}
