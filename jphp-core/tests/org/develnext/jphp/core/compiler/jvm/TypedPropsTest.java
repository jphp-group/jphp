package org.develnext.jphp.core.compiler.jvm;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.annotation.Reflection.Ignore;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TypedPropsTest extends JvmCompilerCase {
    @Test
    public void testBasic(){
        check("typed_props/typed_properties_001.phpt", true);
    }

    @Test
    public void testReadUninitialized(){
        check("typed_props/typed_properties_002.phpt", true);
    }

    @Test
    public void testFetchUninitializedByReference(){
        check("typed_props/typed_properties_003.phpt", true);
    }

    @Test
    public void testErrorConditionTypeMismatch() {
        check("typed_props/typed_properties_004.phpt", true);
    }

    @Test
    public void testErrorConditionTypeMismatchObject() {
        check("typed_props/typed_properties_005.phpt", true);
    }

    @Test
    public void testPropertiesInheritance() {
        check("typed_props/typed_properties_006.phpt", true);
        check("typed_props/typed_properties_007.phpt", true);
        check("typed_props/typed_properties_008.phpt", true);
    }

    @Test
    public void testUnsetLeavesPropertiesInUninitialized() {
        check("typed_props/typed_properties_009.phpt", true);
    }

    @Test
    public void testRefFeatures() {
        check("typed_props/typed_properties_010.phpt", true);
        check("typed_props/typed_properties_011.phpt", true);
        check("typed_props/typed_properties_012.phpt", true);
    }

    @Test
    public void testDefault() {
        check("typed_props/typed_properties_013.phpt", true);
        check("typed_props/typed_properties_014.phpt", true);
        check("typed_props/typed_properties_015.phpt", true);
        check("typed_props/typed_properties_016.phpt", true);
    }

    @Test
    public void testDisallowTypes() {
        check("typed_props/typed_properties_017.phpt", true);
    }

    @Test
    public void testReflection() {
        check("typed_props/typed_properties_018.phpt", true);
    }

    @Test
    public void testInc() {
        check("typed_props/typed_properties_019.phpt", true);
    }

    @Test
    public void testStricTypes() {
        check("typed_props/typed_properties_020.phpt", true);
    }

    @Test
    public void testDelayTypeCheckConstant() {
        //check("typed_props/typed_properties_021.phpt", true); // WHY?
        check("typed_props/typed_properties_022.phpt", true);
    }

    @Test
    public void testStatic() {
        check("typed_props/typed_properties_023.phpt", true);
    }

    @Test
    public void testIgnorePrivatePropsDuringInheritance() {
        check("typed_props/typed_properties_024.phpt", true);
    }

    @Test
    public void testSyntax() {
        check("typed_props/typed_properties_025.phpt", true);
    }

    @Test
    public void testInheritTraitsWithTypedProperties() {
        check("typed_props/typed_properties_026.phpt", true);
    }

    @Test
    public void testFloatWidenAtRuntime() {
        check("typed_props/typed_properties_027.phpt", true);
    }

    @Test
    public void testRespectStrictTypes() {
        check("typed_props/typed_properties_028.phpt", true);
        check("typed_props/typed_properties_029.phpt", true);
    }

    @Test
    public void testMagicGetTyped() {
        check("typed_props/typed_properties_030.phpt", true);
    }

    @Test
    public void testCoerceIntToFloatEvenInStrictMode() {
        check("typed_props/typed_properties_031.phpt", true);
    }

    @Test
    public void testReturnByRefIsAllowed() {
        //check("typed_props/typed_properties_032.phpt", true);
    }

    @Test
    public void testYieldReferenceGuard() {
        check("typed_props/typed_properties_033.phpt", true);
    }

    @Test
    public void testRefs() {
        check("typed_props/typed_properties_033.phpt", true);
        // check("typed_props/typed_properties_034.phpt", true); // TODO FIX
    }

    @Test
    public void testInheritanceMustNotChangeType() {
        check("typed_props/typed_properties_035.phpt", true);
    }

    @Test
    public void testUninitialized() {
        check("typed_props/typed_properties_036.phpt", true);
        check("typed_props/typed_properties_037.phpt", true);
    }

    @Test
    public void testOverflow() {
        check("typed_props/typed_properties_038.phpt", true);
    }

    @Test
    public void testMismatchedProperty() {
        check("typed_props/typed_properties_039.phpt", true);
    }

    @Test
    public void testGetMagicOnUnset() {
        check("typed_props/typed_properties_040.phpt", true);
    }

    @Test
    public void testWeakConversionOfStrings() {
        check("typed_props/typed_properties_041.phpt", true);
        check("typed_props/typed_properties_042.phpt", true);
    }

    @Test // SKIP, jphp doesn't support
    public void testRefsMisc() {
       // check("typed_props/typed_properties_043.phpt", true);
       // check("typed_props/typed_properties_044.phpt", true);
    }

    @Test
    public void testMemory_leaks_on_wrong_assignment_to_typed() {
        check("typed_props/typed_properties_046.phpt", true);
    }

    @Test
    public void testNullable_typed_property() {
        check("typed_props/typed_properties_047.phpt", true);
    }

    @Test
    public void testParent_private_property_types_must_be_ignored() {
        check("typed_props/typed_properties_048.phpt", true);
    }

    @Test
    public void testNullable_typed_property2() {
        check("typed_props/typed_properties_049.phpt", true);
    }

    @Test
    public void testWeak_casts_must_not_overwrite_source_variables() {
        check("typed_props/typed_properties_050.phpt", true);
    }

    @Test
    public void testWeak_casts_must_not_leak() {
        check("typed_props/typed_properties_051.phpt", true);
    }

    @Test
    public void testClass_properties_declared_in_eval_must_not_leak() {
        check("typed_props/typed_properties_052.phpt", true);
    }

    @Test
    public void testTyped_properties_disallow_callable() {
        check("typed_props/typed_properties_053.phpt", true);
    }

    @Test
    public void testTyped_properties_disallow_callable_nullable_variant() {
        check("typed_props/typed_properties_054.phpt", true);
    }

    @Test
    public void testTest_assign_to_typed_property_taken_by_reference() {
        //check("typed_props/typed_properties_055.phpt", true);
    }

    @Test
    public void testType_change_in_assign_op_use_after_free() {
        check("typed_props/typed_properties_056.phpt", true);
    }

    @Test
    public void testType_change_in_pre_post_increment_use_after_free() {
        check("typed_props/typed_properties_057.phpt", true);
    }

    @Test
    public void testConstants_in_default_values_of_properties() {
        check("typed_props/typed_properties_058.phpt", true);
    }

    @Test
    public void testNullable_typed_properties_in_traits() {
        check("typed_props/typed_properties_059.phpt", true);
    }

    @Test
    public void testTest_typed_properties_work_fine_with_simple_inheritance() {
        check("typed_props/typed_properties_060.phpt", true);
    }

    @Test
    public void testTyped_property_on_overloaded_by_ref_property() {
        //check("typed_props/typed_properties_061.phpt", true);
        //check("typed_props/typed_properties_062.phpt", true);
        //check("typed_props/typed_properties_063.phpt", true);
        //check("typed_props/typed_properties_064.phpt", true);
        //check("typed_props/typed_properties_065.phpt", true);
        //check("typed_props/typed_properties_065.phpt", true);
        //check("typed_props/typed_properties_066.phpt", true);
        //check("typed_props/typed_properties_067.phpt", true);
        //check("typed_props/typed_properties_068.phpt", true);
        //check("typed_props/typed_properties_069.phpt", true);
        //check("typed_props/typed_properties_070.phpt", true);
        //check("typed_props/typed_properties_071.phpt", true);
        //check("typed_props/typed_properties_072.phpt", true);
    }

    @Test
    public void testTyped_property_must_cast_when_used_with__get() {
        check("typed_props/typed_properties_072.phpt", true);
        //check("typed_props/typed_properties_073.phpt", true);
        //check("typed_props/typed_properties_074.phpt", true);
        //check("typed_props/typed_properties_075.phpt", true);
        //check("typed_props/typed_properties_075.phpt", true);
        //check("typed_props/typed_properties_076.phpt", true);
    }

    @Test
    public void testConverted_values_shall_be_returned_and_not_the_original_value_upon_property_assignment() {
        check("typed_props/typed_properties_077.phpt", true);
        //check("typed_props/typed_properties_078.phpt", true);
        //check("typed_props/typed_properties_079.phpt", true);
    }

    @Test
    public void testAccess_to_typed_static_properties_before_initialization() {
        check("typed_props/typed_properties_080.phpt", true);
        //check("typed_props/typed_properties_081.phpt", true);
        //check("typed_props/typed_properties_082.phpt", true);
    }

    @Test
    public void testTest_array_promotion_does_not_violate_type_restrictions() {
        check("typed_props/typed_properties_083.phpt", true);
    }

    @Test
    public void testImportant_properties_with_different_types_from_traits() {
        check("typed_props/typed_properties_085.phpt", true);
    }

    @Test
    public void testCheck_for_correct_invalidation_of_prop_info_cache_slots() {
        check("typed_props/typed_properties_088.phpt", true);
        check("typed_props/typed_properties_089.phpt", true);
        //check("typed_props/typed_properties_090.phpt", true);
        //check("typed_props/typed_properties_092.phpt", true);
        //check("typed_props/typed_properties_093.phpt", true);
        //check("typed_props/typed_properties_094.phpt", true);
        //check("typed_props/typed_properties_095.phpt", true);
        //check("typed_props/typed_properties_096.phpt", true);
        //check("typed_props/typed_properties_097.phpt", true);
    }

    @Test
    public void testOther() {
        check("typed_props/typed_properties_097.phpt", true);
        //check("typed_props/typed_properties_098.phpt", true);
        //check("typed_props/typed_properties_099.phpt", true);
        check("typed_props/typed_properties_100.phpt", true);
        check("typed_props/typed_properties_101.phpt", true);
        //check("typed_props/typed_properties_102.phpt", true);
        //check("typed_props/typed_properties_103.phpt", true);
        //check("typed_props/typed_properties_104.phpt", true);
        check("typed_props/typed_properties_105.phpt", true);
        //check("typed_props/typed_properties_106.phpt", true);
    }

    @Test
    public void testOnlyJphp() {
        check("typed_props/jphp_typed_props_001.phpt", true);
        check("typed_props/jphp_typed_props_002.phpt", true);
        check("typed_props/jphp_typed_props_003.phpt", true);
        check("typed_props/jphp_typed_props_004.phpt", true);
        check("typed_props/jphp_typed_props_005.phpt", true);
        check("typed_props/jphp_typed_props_006.phpt", true);
    }
}
