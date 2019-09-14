package org.develnext.jphp.zend.ext.standard.date;

import static org.junit.Assert.*;

import org.develnext.jphp.zend.ZendJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DateTimeZoneTest extends ZendJvmTestCase {

    @Test
    @Ignore // order
    public void verifyViaReflection() {
        check("ext/date/DateTimeZone_verify.phpt");
    }

    @Test
    public void construct() {
        check("ext/date/DateTimeZone_construct_basic.phpt");
        // check("ext/date/DateTimeZone_construct_error.phpt");
    }

    @Test
    public void setState() {
        check("ext/date/DateTimeZone_set_state.phpt");
    }

    @Test
    @Ignore
    public void testClone() {
        check("ext/date/DateTimeZone_clone_basic1.phpt");
        check("ext/date/DateTimeZone_clone_basic2.phpt");
        check("ext/date/DateTimeZone_clone_basic3.phpt");
        check("ext/date/DateTimeZone_clone_basic4.phpt");
    }

    @Test
    public void compare() {
        check("ext/date/DateTimeZone_compare_basic1.phpt");
        check("ext/date/DateTimeZone_extends_basic1.phpt");
    }

    @Test
    public void timezones() {
        check("ext/date/bug66985.phpt");
        check("ext/date/bug70277.phpt");
    }
}