package ru.regenix.jphp.compiler.jvm.zend;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import ru.regenix.jphp.compiler.jvm.JvmCompilerCase;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LangTest extends JvmCompilerCase {

    @Test
    public void testAll(){
        check("zend/lang/001.php");
        check("zend/lang/002.php");
        check("zend/lang/003.php");
        check("zend/lang/004.php");
        check("zend/lang/005.php");
        check("zend/lang/006.php");
        check("zend/lang/007.php");
        check("zend/lang/008.php");
        check("zend/lang/009.php");
        check("zend/lang/010.php");
        check("zend/lang/011.php");
        check("zend/lang/012.php");
        check("zend/lang/013.php");
        check("zend/lang/014.php");
        check("zend/lang/015.php");
        check("zend/lang/016.php");
        check("zend/lang/017.php");
        check("zend/lang/018.php");
        check("zend/lang/019.php");
        check("zend/lang/020.php");
        check("zend/lang/021.php");
        check("zend/lang/022.php");

        check("zend/lang/024.php");
        check("zend/lang/025.php");
        check("zend/lang/026.php");
        check("zend/lang/027.php");
        check("zend/lang/028.php");
        check("zend/lang/030.php");

        check("zend/lang/032.php");
        check("zend/lang/033.php");

        check("zend/lang/035.php");
        check("zend/lang/036.php");
        check("zend/lang/037.php");
    }
}
