package zend;

import org.develnext.jphp.zend.ZendJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LangTest extends ZendJvmTestCase {

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
        check("zend/lang/031.php");
        check("zend/lang/032.php");
        check("zend/lang/033.php");

        check("zend/lang/035.php");
        check("zend/lang/036.php");
        check("zend/lang/037.php");

        check("zend/lang/040.php");
        check("zend/lang/041.php");
        check("zend/lang/042.php");


        check("zend/lang/array_shortcut_001.php");
        check("zend/lang/array_shortcut_002.php");
        check("zend/lang/array_shortcut_003.php");
        check("zend/lang/array_shortcut_005.php");
    }

    @Test
    public void testOperators(){
        //check("zend/lang/operators/add_basiclong_64bit.php");
        check("zend/lang/operators/add_variationStr.php");
        check("zend/lang/operators/bitwiseAnd_variationStr.php");
        check("zend/lang/operators/bitwiseNot_variationStr.php");
        check("zend/lang/operators/bitwiseOr_variationStr.php");
        check("zend/lang/operators/bitwiseShiftLeft_variationStr.php");
        check("zend/lang/operators/bitwiseShiftRight_variationStr.php");
        check("zend/lang/operators/bitwiseXor_variationStr.php");
        check("zend/lang/operators/divide_variationStr.php");
        check("zend/lang/operators/modulus_variationStr.php");
        check("zend/lang/operators/multiply_variationStr.php");
        check("zend/lang/operators/negate_variationStr.php");

        check("zend/lang/operators/operator_equals_basic.php");
        check("zend/lang/operators/operator_gt_basic.php");
        check("zend/lang/operators/operator_gt_or_equal_basic.php");
        check("zend/lang/operators/operator_identical_basic.php");
        check("zend/lang/operators/operator_lt_basic.php");
        check("zend/lang/operators/operator_lt_or_equal_basic.php");
        check("zend/lang/operators/operator_notequals_basic.php");
        check("zend/lang/operators/operator_notidentical_basic.php");

        check("zend/lang/operators/postdec_variationStr.php");
        check("zend/lang/operators/postinc_variationStr.php");
        check("zend/lang/operators/predec_variationStr.php");
        check("zend/lang/operators/preinc_variationStr.php");
        check("zend/lang/operators/subtract_variationStr.php");
    }
}
