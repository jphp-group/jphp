package ru.regenix.jphp.compiler.jvm.zend;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import ru.regenix.jphp.compiler.jvm.JvmCompilerCase;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClassesTest extends JvmCompilerCase {

    @Test
    public void testMagicCall(){
        check("zend/classes/__call_001.php");
        check("zend/classes/__call_002.php", true);
        check("zend/classes/__call_003.php");

        check("zend/classes/__call_004.php");
        check("zend/classes/__call_005.php");
        check("zend/classes/__call_006.php");
        check("zend/classes/__call_007.php");
    }

    @Test
    public void testMagicSetGet(){
        check("zend/classes/__set__get_001.php");
        check("zend/classes/__set__get_002.php", true);
        check("zend/classes/__set__get_003.php", true);
        check("zend/classes/__set__get_004.php");
        check("zend/classes/__set__get_005.php");
        check("zend/classes/__set_data_corrupt.php");
    }

    @Test
    public void testAbstract(){
        check("zend/classes/abstract.php", true);
        check("zend/classes/abstract_by_interface_001.php", true);
        check("zend/classes/abstract_by_interface_002.php", true);
        check("zend/classes/abstract_class.php", true);
        check("zend/classes/abstract_derived.php", true);
        check("zend/classes/abstract_final.php", true);
        check("zend/classes/abstract_inherit.php", true);
        check("zend/classes/abstract_not_declared.php", true);
        check("zend/classes/abstract_redeclare.php", true);
        check("zend/classes/abstract_static.php", true);
        check("zend/classes/abstract_user_call.php", true);
    }

    @Test
    public void testArrayConversionKeys(){
        check("zend/classes/array_conversion_keys.php");
    }

    @Test
    public void testAssignOpProperty(){
        check("zend/classes/assign_op_property_001.php");
    }

    @Test
    public void testPrivate(){
        check("zend/classes/private_001.php", true);
        check("zend/classes/private_002.php", true);
        check("zend/classes/private_003.php", true);
        check("zend/classes/private_003b.php", true);
        check("zend/classes/private_004.php", true);
        check("zend/classes/private_004b.php", true);
        check("zend/classes/private_005.php", true);
        check("zend/classes/private_005b.php", true);
        check("zend/classes/private_006.php");
        check("zend/classes/private_006b.php");
        check("zend/classes/private_007.php");
        check("zend/classes/private_007b.php");

        check("zend/classes/private_members.php");
        check("zend/classes/private_redeclare.php", true);
    }

    @Test
    public void testSingleton(){
        check("zend/classes/singleton_001.php");
    }

    @Test
    public void testThis(){
        check("zend/classes/this.php", true);
    }

    @Test
    public void testBugs(){
        check("zend/classes/bug23951.php");
        check("zend/classes/bug24399.php");
        check("zend/classes/bug24445.php");
        check("zend/classes/bug27504.php", true);
        check("zend/classes/bug29446.php", true);
        check("zend/classes/bug63462.php", true);
    }

    @Test
    public void testClass(){
        check("zend/classes/class_abstract.php", true);
        check("zend/classes/class_example.php", true);
        check("zend/classes/class_final.php", true);
        check("zend/classes/class_stdclass.php", true);
    }

    @Test
    public void testClone(){
        check("zend/classes/clone_001.php");
        check("zend/classes/clone_002.php");
        check("zend/classes/clone_003.php");
        check("zend/classes/clone_004.php");
        check("zend/classes/clone_005.php", true);
        check("zend/classes/clone_006.php");
    }

    @Test
    public void testConstants(){
        check("zend/classes/constants_basic_001.php", true);
        check("zend/classes/constants_basic_002.php", true);
        //check("zend/classes/constants_basic_003.php", true);
    }

    @Test
    public void testFinal(){
        check("zend/classes/final.php");
        check("zend/classes/final_abstract.php", true);
        check("zend/classes/final_ctor1.php", true);
        check("zend/classes/final_ctor2.php", true);
        check("zend/classes/final_ctor3.php", true);
        check("zend/classes/final_redeclare.php", true);
    }
}
