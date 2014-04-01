package org.develnext.jphp.zend.ext.json;

import php.runtime.ext.support.compile.ConstantsContainer;

public class JsonConstants extends ConstantsContainer {
    public final static int JSON_HEX_TAG = 1;
    public final static int JSON_HEX_AMP = 2;
    public final static int JSON_HEX_APOS = 4;
    public final static int JSON_HEX_QUOT = 8;
    public final static int JSON_FORCE_OBJECT = 16;
    public final static int JSON_NUMERIC_CHECK = 32;
    public final static int JSON_BIGINT_AS_STRING = 2;
    public final static int JSON_PRETTY_PRINT = 128;
    public final static int JSON_UNESCAPED_SLASHES = 64;
    public final static int JSON_UNESCAPED_UNICODE = 256;

    public final static int JSON_ERROR_NONE = 0;
    public final static int JSON_ERROR_DEPTH = 1;
    public final static int JSON_ERROR_STATE_MISMATCH = 2;
    public final static int JSON_ERROR_CTRL_CHAR = 3;
    public final static int JSON_ERROR_SYNTAX = 4;
    public final static int JSON_ERROR_UTF8 = 5;
}
