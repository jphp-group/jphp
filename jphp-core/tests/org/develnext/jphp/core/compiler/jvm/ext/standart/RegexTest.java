package org.develnext.jphp.core.compiler.jvm.ext.standart;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegexTest extends JvmCompilerCase {

    @Test
    public void testBasic() {
        check("ext/standard/regex/basic_001.php");
        check("ext/standard/regex/basic_002.php");
        check("ext/standard/regex/basic_003.php");
        check("ext/standard/regex/basic_004.php");
        check("ext/standard/regex/basic_005.php");
        check("ext/standard/regex/basic_006.php");
        check("ext/standard/regex/basic_007.php");
        check("ext/standard/regex/basic_008.php");
        check("ext/standard/regex/basic_009.php");
    }
}
