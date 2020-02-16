package org.develnext.jphp.core.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.Memory;
import php.runtime.memory.ArrayMemory;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ListTest extends JvmCompilerCase {
    @Test
    public void testGeneral(){
        check("list/list_reference_001_1.phpt");
        check("list/list_reference_001_2.phpt");
        check("list/list_reference_001_3.phpt");
        check("list/list_reference_001_4.phpt");
        check("list/list_reference_001_5.phpt");
    }

    @Test
    public void testNewReference() {
        check("list/list_reference_002.phpt");
    }

    @Test
    public void testFromFunction() {
        check("list/list_reference_003.phpt");
    }

    @Test
    public void testForeach() {
        check("list/list_reference_004.phpt");
    }

    @Test
    public void testClassPropertyAndMethods() {
        check("list/list_reference_005.phpt");
    }

    @Test
    public void testClassArrayAccessNoReference() {
        check("list/list_reference_006.phpt");
    }

    @Test
    public void testClassArrayAccessWithReference() {
        check("list/list_reference_007.phpt");
    }

    @Test
    public void testReferenceUnpackingOddities() {
        check("list/list_reference_008.phpt");
    }

    @Test
    public void testReferenceUnpackingVMSafety() {
        check("list/list_reference_009.phpt");
    }

    @Test
    public void testReferenceUnpackingCompileError_scalar() {
        check("list/list_reference_010.phpt", true);
    }

    @Test
    public void testReferenceUnpackingCompileError_const() {
        check("list/list_reference_011.phpt", true);
    }
}
