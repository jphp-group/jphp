package php.runtime.ext.core;

import php.runtime.Information;
import php.runtime.common.Constants;
import php.runtime.ext.support.compile.ConstantsContainer;

public class InfoConstants extends ConstantsContainer {
    public static final String JPHP_VERSION = Information.CORE_VERSION;
    public static String PATH_SEPARATOR = Constants.PATH_SEPARATOR;
    public static String DIRECTORY_SEPARATOR = Constants.DIRECTORY_SEPARATOR;
}
