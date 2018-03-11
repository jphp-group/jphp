package zend;

import org.develnext.jphp.zend.ZendJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OutputTest extends ZendJvmTestCase {

    @Test
    public void testBasic(){
        check("zend/output/ob_001.php");
        check("zend/output/ob_002.php");
        check("zend/output/ob_003.php");
        check("zend/output/ob_004.php");
        check("zend/output/ob_005.php");
        check("zend/output/ob_006.php");
        check("zend/output/ob_007.php");
        check("zend/output/ob_008.php");
        check("zend/output/ob_009.php");
        check("zend/output/ob_010.php");
        check("zend/output/ob_011.php", true);
        check("zend/output/ob_012.php");
        check("zend/output/ob_013.php");
        check("zend/output/ob_014.php");
        check("zend/output/ob_015.php");
        check("zend/output/ob_017.php");
    }

    @Test
    public void testCleanBasic(){
        check("zend/output/ob_clean_basic_001.php");
        check("zend/output/ob_clean_error_001.php");
        check("zend/output/ob_end_clean_basic_001.php");
        check("zend/output/ob_end_clean_error_001.php");
    }

    @Test
    public void testFlushBasic(){
        check("zend/output/ob_end_flush_basic_001.php");
        check("zend/output/ob_end_flush_error_001.php");
        check("zend/output/ob_flush_basic_001.php");
        check("zend/output/ob_flush_error_001.php");
    }

    @Test
    public void testGetCleanBasic(){
        check("zend/output/ob_get_clean_basic_001.php");
        check("zend/output/ob_get_clean_basic_002.php");
        check("zend/output/ob_get_clean_error_001.php");
    }

    @Test
    public void testGetContentsBasic(){
        check("zend/output/ob_get_contents_basic_001.php");
        check("zend/output/ob_get_contents_error_001.php");
    }

    @Test
    public void testGetLengthBasic(){
        check("zend/output/ob_get_length_basic_001.php");
        check("zend/output/ob_get_length_error_001.php");
    }

    @Test
    public void testGetLevelBasic(){
        check("zend/output/ob_get_level_basic_001.php");
        check("zend/output/ob_get_level_error_001.php");
    }

    @Test
    public void testGetStatus(){
        check("zend/output/ob_get_status.php");
    }

    @Test
    public void testImplicitFlushBasic(){
        check("zend/output/ob_implicit_flush_basic_001.php");
        check("zend/output/ob_implicit_flush_basic_002.php");
        check("zend/output/ob_implicit_flush_error_001.php");
        check("zend/output/ob_implicit_flush_variation_001.php");
    }

    @Test
    public void testStartBasic(){
        check("zend/output/ob_start_basic_001.php");
        check("zend/output/ob_start_basic_002.php");
        check("zend/output/ob_start_basic_003.php", true);
        check("zend/output/ob_start_basic_004.php", true);
        check("zend/output/ob_start_basic_005.php");
        check("zend/output/ob_start_basic_006.php");

        check("zend/output/ob_start_basic_unerasable_001.php");

        check("zend/output/ob_start_callbacks.php");

        check("zend/output/ob_start_error_001.php");
        check("zend/output/ob_start_error_002.php");
        check("zend/output/ob_start_error_003.php");
        check("zend/output/ob_start_error_004.php");
        check("zend/output/ob_start_error_005.php", true);
    }
}
