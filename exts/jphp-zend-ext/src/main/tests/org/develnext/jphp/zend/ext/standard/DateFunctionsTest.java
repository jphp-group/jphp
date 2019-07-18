package org.develnext.jphp.zend.ext.standard;

import org.develnext.jphp.zend.ZendJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DateFunctionsTest extends ZendJvmTestCase {

    @Test
    public void dateDefaultTimezoneGet() {
        check("ext/date/date_default_timezone_get-2.phpt");
        check("ext/date/date_default_timezone_get-3.phpt");
        check("ext/date/date_default_timezone_get-4.phpt");
    }

    @Test
    public void dateDefaultTimezoneSet() {
        check("ext/date/date_default_timezone_set_error.phpt");
    }

    @Test
    public void testlocaltime() {
        check("ext/date/007.phpt");
        check("ext/date/localtime_basic.phpt");
        check("ext/date/localtime_variation4.phpt");
        check("ext/date/localtime_variation5.phpt");
    }

    @Test
    public void testMkTime() {
        check("ext/date/bug21966.phpt");
        check("ext/date/gmmktime_basic.phpt");
        check("ext/date/mktime_basic1.phpt");
    }

    @Test
    public void testTime() {
        check("ext/date/time_basic.phpt");
    }

    @Test
    public void testStrftime() {
        check("ext/date/strftime/strftime_basic.phpt");
        check("ext/date/strftime/strftime_variation3.phpt");
        check("ext/date/strftime/strftime_variation4.phpt");
        check("ext/date/strftime/strftime_variation5.phpt");
        check("ext/date/strftime/strftime_variation6.phpt");
        check("ext/date/strftime/strftime_variation7.phpt");
        check("ext/date/strftime/strftime_variation8.phpt");
        check("ext/date/strftime/strftime_variation9.phpt");
        check("ext/date/strftime/strftime_variation10.phpt");
        check("ext/date/strftime/strftime_variation11.phpt");
        check("ext/date/strftime/strftime_variation12.phpt");
        check("ext/date/strftime/strftime_variation13.phpt");
        check("ext/date/strftime/strftime_variation14.phpt");
        check("ext/date/strftime/strftime_variation15.phpt");
        check("ext/date/strftime/strftime_variation16.phpt");
        check("ext/date/strftime/strftime_variation17.phpt");
        check("ext/date/strftime/strftime_variation18.phpt");
        check("ext/date/strftime/strftime_variation19.phpt");
        check("ext/date/strftime/strftime_variation20.phpt");
        check("ext/date/strftime/strftime_variation21.phpt");
        check("ext/date/strftime/strftime_variation22.phpt");
    }

    @Test
    public void testGmStrftime() {
        check("ext/date/strftime/gmstrftime_basic.phpt");
        check("ext/date/strftime/gmstrftime_variation3.phpt");
        check("ext/date/strftime/gmstrftime_variation4.phpt");
        check("ext/date/strftime/gmstrftime_variation5.phpt");
        check("ext/date/strftime/gmstrftime_variation6.phpt");
        check("ext/date/strftime/gmstrftime_variation7.phpt");
        check("ext/date/strftime/gmstrftime_variation8.phpt");
        check("ext/date/strftime/gmstrftime_variation9.phpt");
        check("ext/date/strftime/gmstrftime_variation10.phpt");
        check("ext/date/strftime/gmstrftime_variation11.phpt");
        check("ext/date/strftime/gmstrftime_variation12.phpt");
        check("ext/date/strftime/gmstrftime_variation13.phpt");
        check("ext/date/strftime/gmstrftime_variation14.phpt");
        check("ext/date/strftime/gmstrftime_variation15.phpt");
        check("ext/date/strftime/gmstrftime_variation16.phpt");
        check("ext/date/strftime/gmstrftime_variation17.phpt");
        check("ext/date/strftime/gmstrftime_variation18.phpt");
        check("ext/date/strftime/gmstrftime_variation19.phpt");
        check("ext/date/strftime/gmstrftime_variation20.phpt");
        check("ext/date/strftime/gmstrftime_variation21.phpt");
        check("ext/date/strftime/gmstrftime_variation22.phpt");
    }
}