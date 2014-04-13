package org.develnext.jphp.core.compiler.jvm.ext.standart;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CursorTest extends JvmCompilerCase {

    @Test
    public void testBasic() {
        check("ext/standard/cursor/basic_001.php");
        check("ext/standard/cursor/basic_002.php");
        check("ext/standard/cursor/basic_003.php");
        check("ext/standard/cursor/basic_004.php");
        check("ext/standard/cursor/basic_005.php");
        check("ext/standard/cursor/basic_006.php");
        check("ext/standard/cursor/basic_007.php");
        check("ext/standard/cursor/basic_008.php");
        check("ext/standard/cursor/basic_009.php");
        check("ext/standard/cursor/basic_010.php");
        check("ext/standard/cursor/basic_011.php");
    }

    @Test
    public void testToArray() {
        check("ext/standard/cursor/toarray_001.php");
    }
}
