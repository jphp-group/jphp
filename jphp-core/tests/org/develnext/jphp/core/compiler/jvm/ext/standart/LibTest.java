package org.develnext.jphp.core.compiler.jvm.ext.standart;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LibTest extends JvmCompilerCase {

    @Test
    public void testStr() {
        check("ext/standard/lib/str_001.php");
        check("ext/standard/lib/str_002.php");
        check("ext/standard/lib/str_003.php");
        check("ext/standard/lib/str_004.php");
    }

    @Test
    public void testNum() {
        check("ext/standard/lib/num_001.php");
        check("ext/standard/lib/num_002.php");
        check("ext/standard/lib/num_003.php");
    }

}
