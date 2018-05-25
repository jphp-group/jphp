package org.develnext.jphp.core.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.Memory;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UnsetTest extends JvmCompilerCase {

    @Test
    public void testVariables(){
        Memory memory = includeResource("unset/variables.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testRefVariables(){
        Memory memory = includeResource("unset/ref_variables.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testRefInMethodVariables(){
        Memory memory = includeResource("unset/ref_in_method_variables.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testArrayValue(){
        Memory memory = includeResource("unset/array_value.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testRefArrayValue(){
        Memory memory = includeResource("unset/ref_array_value.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testObjectValue(){
        Memory memory = includeResource("unset/object_value.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testComplex(){
        Memory memory = includeResource("unset/complex.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testBug260() {
        check("unset/bug260.php");
    }
}
