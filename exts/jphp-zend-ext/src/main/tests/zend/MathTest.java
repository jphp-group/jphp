package zend;

import org.develnext.jphp.zend.ZendJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MathTest extends ZendJvmTestCase {
    @Test
    public void testIndivIssue422() {
        check("zend/math/intdiv_issue422.phpt");
    }
}
