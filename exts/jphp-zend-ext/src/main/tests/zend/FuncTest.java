package zend;

import org.develnext.jphp.zend.ZendJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FuncTest extends ZendJvmTestCase {

    @Test
    public void testStrLen(){
        check("zend/func/001_strlen.php");
    }

    @Test
    public void testStaticVariablesInFuncs(){
        check("zend/func/002_static_variables_in_funcs.php");
    }

    @Test
    public void testGeneral1(){
        check("zend/func/003_general.php");
    }

    @Test
    public void testGeneral2(){
        check("zend/func/004_general.php");
    }

    @Test
    public void testIni(){
        check("zend/func/007_ini_test.php");
    }

    @Test
    public void testIniAlter(){
        check("zend/func/ini_alter.php");
    }
}
