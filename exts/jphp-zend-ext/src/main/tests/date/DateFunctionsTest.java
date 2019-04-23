package date;

import java.time.Duration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.develnext.jphp.zend.ZendJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DateFunctionsTest extends ZendJvmTestCase {
    @Test
    public void testcheckdate() {
        check("ext/date/checkdate_basic1.phpt");
        check("ext/date/006.phpt");
    }

    @Test
    public void testgetdate() {
        check("ext/date/008.phpt");
    }

    @Test
    public void teststrftime() {

        // check("ext/date/gmstrftime_variation3.phpt");

        // prinf or sscanf bug
        // check("ext/date/gmstrftime_variation15.phpt");
        // check("ext/date/gmstrftime_variation16.phpt");

        check("ext/date/gmstrftime_variation5.phpt");
        check("ext/date/009.phpt");
        check("ext/date/gmstrftime_basic.phpt");
        check("ext/date/gmstrftime_variation20.phpt");
        check("ext/date/gmstrftime_variation4.phpt");
        check("ext/date/gmstrftime_variation6.phpt");
        check("ext/date/gmstrftime_variation7.phpt");
        check("ext/date/gmstrftime_variation8.phpt");
        check("ext/date/gmstrftime_variation9.phpt");
        check("ext/date/gmstrftime_variation10.phpt");
        check("ext/date/gmstrftime_variation11.phpt");
        check("ext/date/gmstrftime_variation13.phpt");
        check("ext/date/gmstrftime_variation14.phpt");
        check("ext/date/gmstrftime_variation17.phpt");
        check("ext/date/gmstrftime_variation18.phpt");
        check("ext/date/gmstrftime_variation19.phpt");
        check("ext/date/gmstrftime_variation21.phpt");
        check("ext/date/gmstrftime_variation22.phpt");
    }

    @Test
    public void testlocaltime() {
        check("ext/date/007.phpt");
        check("ext/date/localtime_basic.phpt");
        check("ext/date/localtime_variation4.phpt");
        check("ext/date/localtime_variation5.phpt");
    }

    @Test
    public void testDate() {
        check("ext/date/003.phpt");
        check("ext/date/004.phpt");
    }

    @Test
    public void testDateDefaultTimezoneGet() {
        check("ext/date/date_default_timezone_get-1.phpt");
        check("ext/date/date_default_timezone_get-2.phpt");
        check("ext/date/date_default_timezone_get-3.phpt");
        check("ext/date/date_default_timezone_get-4.phpt");
    }

    @Test
    public void testMkTime() {
        check("ext/date/bug21966.phpt");
        check("ext/date/mktime-1.phpt");
        check("ext/date/mktime_basic1.phpt");
        check("ext/date/mktime-3-64bit.phpt");
        check("ext/date/mktime_no_args.phpt");
        check("ext/date/gmmktime_basic.phpt");
    }

    @Test
    public void testDateCreate() {
        check("ext/date/date_create-relative.phpt");
        check("ext/date/date_create-2.phpt");
        check("ext/date/date_create_basic.phpt");
        check("ext/date/date_create_from_format_basic2.phpt");
        check("ext/date/date_create-1.phpt");
    }

    @Test
    public void strtotime() {
        check("ext/date/002.phpt");
        check("ext/date/strtotime.phpt");
        check("ext/date/strtotime2.phpt");
        check("ext/date/strtotime3-64bit.phpt");
        check("ext/date/strtotime-mysql-64bit.phpt");
        check("ext/date/strtotime_basic.phpt");
        check("ext/date/strtotime_basic2.phpt");
        check("ext/date/strtotime-relative.phpt");
        check("ext/date/bug26198.phpt");
        check("ext/date/bug13142.phpt");
        check("ext/date/bug14561.phpt");
        check("ext/date/bug17988.phpt");
        check("ext/date/bug21399.phpt");
        check("ext/date/bug26320.phpt");
        check("ext/date/bug28088.phpt");
        check("ext/date/bug28599.phpt");
        check("ext/date/bug29150.phpt");
        check("ext/date/bug29595.phpt");
        check("ext/date/bug30096.phpt");
        check("ext/date/bug32588.phpt");
        check("ext/date/bug33056.phpt");
        check("ext/date/bug33452.phpt");
        check("ext/date/bug33532.phpt");
        check("ext/date/bug32555.phpt");
        check("ext/date/bug32555.phpt");
        check("ext/date/bug32270.phpt");
        check("ext/date/bug26090.phpt");
        check("ext/date/bug26694.phpt");
        check("ext/date/bug26317.phpt");
        check("ext/date/bug27780.phpt");

        // fail
        check("ext/date/bug28024.phpt");
        check("ext/date/bug30532.phpt");
        check("ext/date/bug32086.phpt");
        check("ext/date/bug33415-1.phpt");
        check("ext/date/bug33414-1.phpt");
        check("ext/date/bug33414-2.phpt");
        check("ext/date/bug29585.phpt");
        check("ext/date/bug33415-2.phpt");
        check("ext/date/bug20382-1.phpt");
    }

    @Test
    public void strtotimeFails() {

    }

    @Test
    public void testDateDateSet() {
        check("ext/date/013.phpt");
    }

    @Test
    @Ignore
    public void testDateDefaultTimezoneSet() {
        check("ext/date/date_default_timezone_set-1.phpt");
        check("ext/date/date_default_timezone_set_error.phpt");
    }

    @Test
    public void testTimezoneAbbreviationsList() {
        check("ext/date/010.phpt");
    }

    @Test
    public void testTimezoneNameFromAbbr() {
        check("ext/date/011.phpt");
    }
}
