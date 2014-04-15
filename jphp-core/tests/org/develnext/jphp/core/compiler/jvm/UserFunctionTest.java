package org.develnext.jphp.core.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.memory.ArrayMemory;
import php.runtime.Memory;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserFunctionTest extends JvmCompilerCase {

    @Test
    public void testBasic(){
        Memory memory = includeResource("user_function/basic.php");
        Assert.assertEquals(20, memory.toLong());
    }

    @Test
    public void testWithParams(){
        Memory memory = includeResource("user_function/with_params.php");
        Assert.assertEquals(6, memory.toLong());
    }

    @Test
    public void testWithGlobals(){
        ArrayMemory globals = new ArrayMemory();
        globals.refOfIndex("y").assign("bar");

        Memory memory = includeResource("user_function/with_globals.php", globals);
        Assert.assertEquals("foobar", memory.toString());
    }

    @Test
    public void testWithSimpleDefault(){
        includeResource("user_function/with_simple_default.php");
        Assert.assertEquals(
                "int(100)\n" +
                "float(2.3)\n" +
                "string(6) \"foobar\"\n" +
                "bool(true)\n" +
                "NULL\n",
        getOutput());
    }


    @Test
    public void testWithArrayDefault(){
        Memory memory = includeResource("user_function/with_array_default.php");
        Assert.assertEquals(10, memory.toLong());
    }

    @Test
    public void testWithReferences(){
        Memory memory = includeResource("user_function/with_references.php");
        Assert.assertEquals("foobar", memory.toString());
    }

    @Test
    public void testArrayReferences(){
        Memory memory = includeResource("user_function/with_array_references.php");
        Assert.assertEquals("100500|200600", memory.toString());
    }

    @Test
    public void testArrayReferenceReturn(){
        Memory memory = includeResource("user_function/with_array_reference_return.php");
        Assert.assertEquals(40, memory.toLong());
    }

    @Test
    public void testArrayReferenceDefault(){
        Memory memory = includeResource("user_function/with_reference_default.php");
        Assert.assertEquals(100520, memory.toLong());
    }

    @Test
    public void testStaticVariables(){
        Memory memory = includeResource("user_function/static_variables.php");
        Assert.assertEquals(33, memory.toLong());
    }

    @Test
    public void testWithConstDefault(){
        Memory memory = includeResource("user_function/with_const_default.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testNested(){
        Memory memory = includeResource("user_function/nested.php");
        Assert.assertEquals("success", memory.toString());
    }
}
