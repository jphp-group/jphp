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
        check("typed_props/typed_properties_001.phpt");
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
    @Ignore
    public void testPropertiesInheritance() {
        /*check("typed_props/typed_properties_006.phpt", true);
        check("typed_props/typed_properties_007.phpt", true);
        check("typed_props/typed_properties_008.phpt", true);*/
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
    public void testMagic() {
        // TODO
        // check("typed_props/typed_properties_030.phpt", true);
    }

    @Test
    public void testCoerceIntToFloatEvenInStrictMode() {
        check("typed_props/typed_properties_031.phpt", true);
    }

    @Test
    public void testReturnByRefIsAllowed() {
        check("typed_props/typed_properties_032.phpt", true);
    }

    @Test
    public void testYieldReferenceGuard() {
        //check("typed_props/typed_properties_033.phpt", true);
    }

}
