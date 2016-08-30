package org.develnext.jphp.zend.ext.standard;

import php.runtime.ext.support.compile.ConstantsContainer;

public class FileConstants extends ConstantsContainer {
    public final static int FILE_USE_INCLUDE_PATH = 1;
    public final static int FILE_APPEND = 8;
    public final static int LOCK_EX = 2;
    public final static int FILE_IGNORE_NEW_LINES = 2;
    public final static int FILE_SKIP_EMPTY_LINES = 4;

    public final static int PATHINFO_DIRNAME = 1;
    public final static int PATHINFO_BASENAME = 2;
    public final static int PATHINFO_EXTENSION = 3;
    public final static int PATHINFO_FILENAME = 4;

    public final static int SEEK_END = 2;
    public final static int SEEK_CUR = 1;
    public final static int SEEK_SET = 0;

    public final static int SCANDIR_SORT_ASCENDING = 0;
    public final static int SCANDIR_SORT_DESCENDING = 1;
    public final static int SCANDIR_SORT_NONE = 2;
}
