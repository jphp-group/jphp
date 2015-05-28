package org.develnext.jphp.core.compiler.jvm;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GrammarTest extends JvmCompilerCase {

    @Test
    public void testRegression() {
        check("grammar/regression_001.php");
        check("grammar/regression_002.php");
        check("grammar/regression_003.php", true);
        check("grammar/regression_004.php", true);
        check("grammar/regression_005.php", true);
        check("grammar/regression_006.php");
        // 007
        check("grammar/regression_008.php");
        check("grammar/regression_009.php");
        // check("grammar/regression_010.php"); skip
        // check("grammar/regression_011.php"); skip
        check("grammar/regression_012.php", true);
        check("grammar/regression_013.php");
    }

    @Test
    public void testSemiReserved() {
        check("grammar/semi_reserved_001.php");
        check("grammar/semi_reserved_002.php");
        check("grammar/semi_reserved_003.php");
        check("grammar/semi_reserved_004.php");
        check("grammar/semi_reserved_005.php");
        check("grammar/semi_reserved_006.php");
        check("grammar/semi_reserved_007.php");
        check("grammar/semi_reserved_008.php");
        check("grammar/semi_reserved_009.php");
        check("grammar/semi_reserved_010.php");
    }
}
