package zend;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.develnext.jphp.zend.ZendJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClassesTest extends ZendJvmTestCase {

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
        check("zend/classes/constants_basic_003.php", true);
        check("zend/classes/constants_basic_004.php", true);
        check("zend/classes/constants_basic_005.php", true);
        // check("zend/classes/constants_basic_006.php", true);

        check("zend/classes/constants_error_001.php", true);
        check("zend/classes/constants_error_002.php", true);
        check("zend/classes/constants_error_003.php", true);
        check("zend/classes/constants_error_004.php", true);
        check("zend/classes/constants_error_005.php", true);
        check("zend/classes/constants_error_006.php", true);
        check("zend/classes/constants_error_007.php", true);

        check("zend/classes/constants_scope_001.php");
    }

    @Test
    public void testDereferencing(){
        check("zend/classes/dereferencing_001.php");
    }

    @Test
    public void testCtorDtor(){
        check("zend/classes/ctor_dtor.php");
        check("zend/classes/ctor_dtor_inheritance.php");
        check("zend/classes/ctor_failure.php");
        check("zend/classes/ctor_in_interface_01.php", true);
        check("zend/classes/ctor_in_interface_02.php", true);
        check("zend/classes/ctor_in_interface_03.php", true);
        check("zend/classes/ctor_in_interface_04.php", true);
        check("zend/classes/ctor_name_clash.php", true);
        check("zend/classes/ctor_visibility.php", true);
    }

    @Test
    public void testDestructor(){
        check("zend/classes/destructor_and_echo.php");
        check("zend/classes/destructor_and_exceptions.php");
        check("zend/classes/destructor_and_globals.php");
        check("zend/classes/destructor_and_references.php");
        check("zend/classes/destructor_inheritance.php");
    }

    @Test
    public void testFactory(){
        check("zend/classes/factory_001.php");
        check("zend/classes/factory_and_singleton_001.php");
        check("zend/classes/factory_and_singleton_002.php");
        check("zend/classes/factory_and_singleton_003.php", true);
        check("zend/classes/factory_and_singleton_004.php", true);
        check("zend/classes/factory_and_singleton_007.php", true);
        check("zend/classes/factory_and_singleton_008.php", true);
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

    @Test
    public void testIncdecProperty(){
        check("zend/classes/incdec_property_001.php");
        check("zend/classes/incdec_property_002.php");
        check("zend/classes/incdec_property_003.php");
        check("zend/classes/incdec_property_004.php");
    }

    @Test
    public void testInheritance(){
        check("zend/classes/inheritance.php");
        check("zend/classes/inheritance_002.php");
        check("zend/classes/inheritance_003.php", true);
        check("zend/classes/inheritance_004.php", true);
        check("zend/classes/inheritance_005.php", true);
        check("zend/classes/inheritance_006.php", true);
    }

    @Test
    public void testInterface(){
        check("zend/classes/interface_and_extends.php", true);
        check("zend/classes/interface_class.php", true);
        check("zend/classes/interface_constant_inheritance_001.php", true);
        check("zend/classes/interface_constant_inheritance_002.php", true);
        check("zend/classes/interface_constant_inheritance_003.php", true);
        check("zend/classes/interface_constant_inheritance_004.php", true);
        check("zend/classes/interface_doubled.php", true);
        check("zend/classes/interface_implemented.php", true);
        check("zend/classes/interface_instantiate.php", true);
        check("zend/classes/interface_member.php", true);
        check("zend/classes/interface_method.php", true);
        check("zend/classes/interface_method_final.php", true);
        check("zend/classes/interface_method_private.php", true);
        check("zend/classes/interface_must_be_implemented.php", true);
        check("zend/classes/interface_optional_arg.php", true);
        check("zend/classes/interface_optional_arg_002.php", true);
        check("zend/classes/interface_optional_arg_003.php", true);

        check("zend/classes/interfaces_001.php", true);
        check("zend/classes/interfaces_002.php", true);
        check("zend/classes/interfaces_003.php", true);
    }

    @Test
    public void testIterators(){
        check("zend/classes/iterators_001.php", true);
        check("zend/classes/iterators_002.php", true);
        check("zend/classes/iterators_003.php", true);
        check("zend/classes/iterators_004.php", true);
        check("zend/classes/iterators_005.php", true);
        check("zend/classes/iterators_006.php", true);
        check("zend/classes/iterators_007.php", true);
        check("zend/classes/iterators_008.php", true);
    }

    @Test
    public void testMethodCallVariation(){
        check("zend/classes/method_call_variation_001.php");
    }

    @Test
    public void testMethodOverrideOptional(){
        check("zend/classes/method_override_optional_arg_001.php");
        check("zend/classes/method_override_optional_arg_002.php");
    }

    @Test
    public void testObjectReference(){
        check("zend/classes/object_reference_001.php");
    }

    @Test
    public void testPropertyOverride(){
        check("zend/classes/property_override_privateStatic_private.php");
        check("zend/classes/property_override_privateStatic_privateStatic.php");
        check("zend/classes/property_override_privateStatic_protected.php");
        check("zend/classes/property_override_privateStatic_protectedStatic.php");
        check("zend/classes/property_override_privateStatic_public.php");
        check("zend/classes/property_override_privateStatic_publicStatic.php");
        check("zend/classes/property_override_private_private.php");
        check("zend/classes/property_override_private_privateStatic.php");
        check("zend/classes/property_override_private_protected.php");
        check("zend/classes/property_override_private_protectedStatic.php");
        check("zend/classes/property_override_private_public.php");
        check("zend/classes/property_override_private_publicStatic.php");
        check("zend/classes/property_override_protectedStatic_private.php", true);
        check("zend/classes/property_override_protectedStatic_privateStatic.php", true);
        check("zend/classes/property_override_protectedStatic_protected.php", true);
        check("zend/classes/property_override_protectedStatic_protectedStatic.php");
        check("zend/classes/property_override_protectedStatic_public.php", true);
        check("zend/classes/property_override_protectedStatic_publicStatic.php");
        check("zend/classes/property_override_protected_private.php", true);
        check("zend/classes/property_override_protected_privateStatic.php", true);
        check("zend/classes/property_override_protected_protected.php");
        check("zend/classes/property_override_protected_protectedStatic.php", true);
        check("zend/classes/property_override_protected_public.php");
        check("zend/classes/property_override_protected_publicStatic.php", true);
        check("zend/classes/property_override_publicStatic_private.php", true);
        check("zend/classes/property_override_publicStatic_privateStatic.php", true);
        check("zend/classes/property_override_publicStatic_protected.php", true);
        check("zend/classes/property_override_publicStatic_protectedStatic.php", true);
        check("zend/classes/property_override_publicStatic_public.php", true);
        check("zend/classes/property_override_publicStatic_publicStatic.php");
        check("zend/classes/property_override_public_private.php", true);
        check("zend/classes/property_override_public_privateStatic.php", true);
        check("zend/classes/property_override_public_protected.php", true);
        check("zend/classes/property_override_public_protectedStatic.php", true);
        check("zend/classes/property_override_public_public.php");
        check("zend/classes/property_override_public_publicStatic.php", true);
    }

    @Test
    public void testPropertyRecreate(){
        check("zend/classes/property_recreate_private.php", true);
        check("zend/classes/property_recreate_protected.php", true);
    }

    @Test
    public void testProtected(){
        check("zend/classes/protected_001.php", true);
        check("zend/classes/protected_001b.php", true);
        check("zend/classes/protected_002.php", true);
    }

    @Test
    public void testStaticMix(){
        check("zend/classes/static_mix_1.php", true);
        check("zend/classes/static_mix_2.php", true);
    }

    @Test
    public void testStaticProperties(){
        check("zend/classes/static_properties_001.php");
        check("zend/classes/static_properties_003.php");
        check("zend/classes/static_properties_003_error1.php", true);
        check("zend/classes/static_properties_003_error2.php", true);
        check("zend/classes/static_properties_003_error3.php", true);
        check("zend/classes/static_properties_003_error4.php", true);
        check("zend/classes/static_properties_004.php");
        check("zend/classes/static_properties_undeclared_assign.php", true);
        check("zend/classes/static_properties_undeclared_assignInc.php", true);
        check("zend/classes/static_properties_undeclared_assignRef.php", true);
        check("zend/classes/static_properties_undeclared_inc.php", true);
        check("zend/classes/static_properties_undeclared_isset.php");
        check("zend/classes/static_properties_undeclared_read.php", true);
    }

    @Test
    public void testStaticThis(){
        check("zend/classes/static_this.php", true);
    }

    @Test
    public void testToString(){
        check("zend/classes/tostring_001.php");
        check("zend/classes/tostring_002.php");
        check("zend/classes/tostring_003.php");
        check("zend/classes/tostring_004.php");
    }

    @Test
    public void testTypeHinting(){
        check("zend/classes/type_hinting_001.php", true);
        check("zend/classes/type_hinting_002.php", true);
        check("zend/classes/type_hinting_003.php", true);
        check("zend/classes/type_hinting_004.php", true);

        check("zend/classes/type_hinting_005a.php");
        check("zend/classes/type_hinting_005b.php");
        check("zend/classes/type_hinting_005c.php");
        check("zend/classes/type_hinting_005d.php");
    }

    @Test
    public void testUnsetProperties(){
        check("zend/classes/unset_properties.php");
    }

    @Test
    public void testVisibility(){
        check("zend/classes/visibility_000a.php", true);
        check("zend/classes/visibility_000b.php", true);
        check("zend/classes/visibility_000c.php", true);

        check("zend/classes/visibility_001a.php", true);
        check("zend/classes/visibility_001b.php", true);
        check("zend/classes/visibility_001c.php", true);

        check("zend/classes/visibility_002a.php", true);
        check("zend/classes/visibility_002b.php", true);
        check("zend/classes/visibility_002c.php", true);

        check("zend/classes/visibility_003a.php", true);
        check("zend/classes/visibility_003b.php", true);
        check("zend/classes/visibility_003c.php", true);

        check("zend/classes/visibility_004a.php", true);
        check("zend/classes/visibility_004b.php", true);
        check("zend/classes/visibility_004c.php", true);

        check("zend/classes/visibility_005.php", true);
    }

    @Test
    public void testArrayAccess(){
        check("zend/classes/array_access_001.php");
        check("zend/classes/array_access_002.php");
        check("zend/classes/array_access_003.php");
        check("zend/classes/array_access_004.php");
        check("zend/classes/array_access_005.php");
        check("zend/classes/array_access_006.php");
        check("zend/classes/array_access_007.php");
        check("zend/classes/array_access_008.php");
        check("zend/classes/array_access_009.php");
        check("zend/classes/array_access_010.php");
        check("zend/classes/array_access_011.php");
        check("zend/classes/array_access_012.php");
        check("zend/classes/array_access_013.php");
    }

    @Test
    public void testAutoload(){
        check("zend/classes/autoload_001.php");
        check("zend/classes/autoload_002.php");
        check("zend/classes/autoload_003.php");
        check("zend/classes/autoload_004.php");
        check("zend/classes/autoload_005.php");
        check("zend/classes/autoload_006.php");
        check("zend/classes/autoload_007.php");
        check("zend/classes/autoload_008.php");
        check("zend/classes/autoload_009.php", true);
        check("zend/classes/autoload_010.php", true);
        check("zend/classes/autoload_011.php", true);

        check("zend/classes/autoload_018.php");
        check("zend/classes/autoload_019.php");

        check("zend/classes/autoload_021.php",true);
    }

    @Test
    public void testDebugInfo() {
        check("zend/classes/__debug_info_001.php");
    }
}
