package zend;

import org.develnext.jphp.zend.ZendJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GotoTest extends ZendJvmTestCase {

    @Test
    public void testLanguage() {
        check("zend/goto/jump01.php");
        check("zend/goto/jump02.php");
        check("zend/goto/jump03.php");
        check("zend/goto/jump04.php");
        check("zend/goto/jump05.php");
        check("zend/goto/jump06.php", true);
        check("zend/goto/jump07.php", true);
        check("zend/goto/jump08.php", true);
        check("zend/goto/jump09.php", true);
        check("zend/goto/jump10.php", true);
        check("zend/goto/jump11.php");
        check("zend/goto/jump13.php");
        check("zend/goto/jump14.php");
        check("zend/goto/jump15_ex.php", true);
    }

    @Test
    public void testFinally() {
        check("zend/goto/finally_goto_001.php", true);
        check("zend/goto/finally_goto_002.php", true);
        check("zend/goto/finally_goto_003.php");
        check("zend/goto/finally_goto_004.php", true);
    }
}
