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
        check("zend/traits/language006.php");
        check("zend/traits/language007.php");
    }
}
