package date;

import org.develnext.jphp.zend.ZendJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DateIntervalTest extends ZendJvmTestCase {
    @Test
    public void all() {
        check("ext/date/DateInterval_basic.phpt");
        check("ext/date/DateInterval_error__construct.phpt");
        check("ext/date/DateInterval_days_prop1.phpt");
        check("ext/date/DateInterval_write_property_return.phpt");
        check("ext/date/DateInterval__set_state.phpt");
        check("ext/date/DateInterval_format.phpt");
        check("ext/date/DateInterval_format_a.phpt");
    }
}
