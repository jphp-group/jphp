package org.develnext.jphp.core.compiler.jvm.zend;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TraitsTest extends JvmCompilerCase {

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
}
