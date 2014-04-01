package zend;

import org.develnext.jphp.zend.ZendJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.exceptions.support.ErrorType;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TraitsTest extends ZendJvmTestCase {

    @Test
    public void testLanguage() {
        check("zend/traits/language001.php");
        check("zend/traits/language002.php");
        check("zend/traits/language003.php");
        check("zend/traits/language004.php");
        check("zend/traits/language005.php");
        check("zend/traits/language006.php");
        check("zend/traits/language007.php");
        check("zend/traits/language008a.php", true);
        check("zend/traits/language008b.php", true);
        check("zend/traits/language009.php");
        check("zend/traits/language010.php", true);
        check("zend/traits/language011.php", true);
        check("zend/traits/language012.php");
        check("zend/traits/language013.php");
        check("zend/traits/language014.php", true);
        check("zend/traits/language015.php", true);
        check("zend/traits/language016.php", true);
        check("zend/traits/language017.php", true);
        check("zend/traits/language018.php", true);
        check("zend/traits/language019.php", true);
    }

    @Test
    public void testInheritance() {
        check("zend/traits/inheritance001.php", ErrorType.E_ALL.value);
        check("zend/traits/inheritance002.php", ErrorType.E_ALL.value);
        check("zend/traits/inheritance003.php", true, ErrorType.E_ALL.value);
    }

    @Test
    public void testInterface() {
        check("zend/traits/interface_001.php", ErrorType.E_ALL.value);
        check("zend/traits/interface_002.php", true, ErrorType.E_ALL.value);
        check("zend/traits/interface_003.php", ErrorType.E_ALL.value);
    }

    @Test
    public void testFlattening() {
        check("zend/traits/flattening001.php", ErrorType.E_ALL.value);
        check("zend/traits/flattening002.php", ErrorType.E_ALL.value);
        check("zend/traits/flattening003.php", ErrorType.E_ALL.value);
    }

    @Test
    public void testMethods() {
        check("zend/traits/methods_001.php");
        check("zend/traits/methods_002.php", true);
        check("zend/traits/methods_003.php");
    }

    @Test
    public void testNoctor() {
        check("zend/traits/noctor001.php");
    }

    @Test
    public void testProperty() {
        check("zend/traits/property001.php", ErrorType.E_ALL.value | ErrorType.E_STRICT.value);
        check("zend/traits/property002.php", ErrorType.E_ALL.value);
        check("zend/traits/property003.php", true);
        check("zend/traits/property004.php", true);
        check("zend/traits/property005.php", true, ErrorType.E_ALL.value | ErrorType.E_STRICT.value);
        check("zend/traits/property006.php", ErrorType.E_ALL.value | ErrorType.E_STRICT.value);
        check("zend/traits/property007.php", ErrorType.E_ALL.value | ErrorType.E_STRICT.value);
        check("zend/traits/property008.php", ErrorType.E_ALL.value | ErrorType.E_STRICT.value);
    }

    @Test
    public void testStatic() {
        check("zend/traits/static_001.php");
        check("zend/traits/static_002.php");
        check("zend/traits/static_003.php");
        check("zend/traits/static_004.php");
    }

    @Test
    public void testGetCalledClass() {
        check("zend/traits/static_get_called_class.php");
    }

    @Test
    public void testTraitConstant() {
        check("zend/traits/trait_constant_001.php");
        check("zend/traits/trait_constant_002.php");
    }

    @Test
    public void testConflict() {
        check("zend/traits/conflict001.php", true);
        check("zend/traits/conflict002.php", ErrorType.E_ALL.value);
        check("zend/traits/conflict003.php", true);
    }

    @Test
    public void testError() {
        check("zend/traits/error_001.php", true);
        check("zend/traits/error_002.php", true);
        check("zend/traits/error_003.php", true);
        check("zend/traits/error_004.php", true);
        check("zend/traits/error_005.php", true);
        check("zend/traits/error_006.php", true);
        check("zend/traits/error_007.php", true);
        check("zend/traits/error_008.php", true);
        check("zend/traits/error_009.php", true);
        check("zend/traits/error_010.php", true);
        check("zend/traits/error_011.php", true);
        check("zend/traits/error_012.php", true);
        check("zend/traits/error_013.php", true);
        check("zend/traits/error_014.php", true);
        check("zend/traits/error_015.php", true);
        check("zend/traits/error_016.php", true);
    }
}
