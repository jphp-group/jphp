package org.develnext.jphp.zend.ext.standard;

import php.runtime.Memory;
import php.runtime.ext.support.compile.ConstantsContainer;
import php.runtime.memory.StringMemory;

public class DateConstants extends ConstantsContainer {
    public static final Memory DATE_ATOM = StringMemory.valueOf("Y-m-d\\TH:i:sP");
    public static final Memory DATE_COOKIE = StringMemory.valueOf("l, d-M-Y H:i:s T");
    public static final Memory DATE_ISO8601 = StringMemory.valueOf("Y-m-d\\TH:i:sO");
    public static final Memory DATE_RFC822 = StringMemory.valueOf("D, d M y H:i:s O");
    public static final Memory DATE_RFC850 = StringMemory.valueOf("l, d-M-y H:i:s T");
    public static final Memory DATE_RFC1036 = StringMemory.valueOf("D, d M y H:i:s O");
    public static final Memory DATE_RFC1123 = StringMemory.valueOf("D, d M Y H:i:s O");
    public static final Memory DATE_RFC2822 = StringMemory.valueOf("D, d M Y H:i:s O");
    public static final Memory DATE_RFC3339 = StringMemory.valueOf("Y-m-d\\TH:i:sP");
    public static final Memory DATE_RFC3339_EXTENDED = StringMemory.valueOf("Y-m-d\\TH:i:s.vP");
    public static final Memory DATE_RFC7231 = StringMemory.valueOf("D, d M Y H:i:s \\G\\M\\T");
    public static final Memory DATE_RSS = StringMemory.valueOf("D, d M Y H:i:s O");
    public static final Memory DATE_W3C = StringMemory.valueOf("Y-m-d\\TH:i:sP");
}
