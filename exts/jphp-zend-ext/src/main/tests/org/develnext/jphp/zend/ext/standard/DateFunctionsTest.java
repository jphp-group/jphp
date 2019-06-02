package org.develnext.jphp.zend.ext.standard;

import org.develnext.jphp.zend.ZendJvmTestCase;
import org.junit.Test;

public class DateFunctionsTest extends ZendJvmTestCase {

    @Test
    public void dateDefaultTimezoneGet() {
        //check("ext/date/date_default_timezone_get-2.phpt");
        check("ext/date/date_default_timezone_get-3.phpt");
        //check("ext/date/date_default_timezone_get-4.phpt");
    }

    @Test
    public void dateDefaultTimezoneSet() {
        check("ext/date/date_default_timezone_set_error.phpt");
    }
}