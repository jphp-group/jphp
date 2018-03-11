package zend.ext.serialize;

import org.develnext.jphp.zend.ZendJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SerializeTest extends ZendJvmTestCase {

    @Test
    public void testMain(){
        check("zend/ext/serialize/001.php");
        check("zend/ext/serialize/002.php");
        check("zend/ext/serialize/003.php");
        check("zend/ext/serialize/004.php");

        // TODO Fix for Maven
//        check("zend/ext/serialize/006.php");

        check("zend/ext/serialize/bug14293.php");
        check("zend/ext/serialize/bug21957.php");
        check("zend/ext/serialize/bug23298.php");
    }

    @Test
    public void testArrays(){
        check("zend/ext/serialize/serialization_arrays_001.php");
    }

    @Test
    public void testMiscTypes(){
        check("zend/ext/serialize/serialization_miscTypes_001.php");
    }
}
