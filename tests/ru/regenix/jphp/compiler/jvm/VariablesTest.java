package ru.regenix.jphp.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import ru.regenix.jphp.runtime.memory.support.Memory;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VariablesTest extends JvmCompilerCase {

    @Test
    public void testSimple(){
        Memory memory = includeResource("variables/simple.php");
        Assert.assertEquals(40, memory.toLong());
    }

    @Test
    public void testGlobal(){
        Memory memory = includeResource("variables/global.php");
        Assert.assertEquals("foobar|foobar", memory.toString());
    }

    @Test
    public void testReference(){
        Memory memory = includeResource("variables/reference.php");
        Assert.assertEquals("foobar|foobar|foobar", memory.toString());
    }

    @Test
    public void testStatic(){
        Memory memory = includeResource("variables/static.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testVarVar(){
        Memory memory = includeResource("variables/var_var.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testCompact(){
        Memory memory = includeResource("variables/compact.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testList(){
        Memory memory = includeResource("variables/list.php");
        Assert.assertEquals("success", memory.toString());
    }
}
