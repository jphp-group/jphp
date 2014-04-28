package org.develnext.jphp.core.compiler.jvm.ext.standart;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FlowTest extends JvmCompilerCase {

    @Test
    public void testBasic() {
        check("ext/standard/flow/basic_001.php");
        check("ext/standard/flow/basic_002.php");
        check("ext/standard/flow/basic_003.php");
        check("ext/standard/flow/basic_004.php");
        check("ext/standard/flow/basic_005.php");
        check("ext/standard/flow/basic_006.php");
        check("ext/standard/flow/basic_007.php");
        check("ext/standard/flow/basic_008.php");
        check("ext/standard/flow/basic_009.php");
        check("ext/standard/flow/basic_010.php");
        check("ext/standard/flow/basic_011.php");
        check("ext/standard/flow/basic_012.php");
    }

    @Test
    public void testSorting() {
        check("ext/standard/flow/sorting_001.php");
        check("ext/standard/flow/sorting_002.php");
    }

    @Test
    public void testToArray() {
        check("ext/standard/flow/toarray_001.php");
    }
}
