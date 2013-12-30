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
public class CastTest extends JvmCompilerCase {

    @Test
    public void testScalar(){
        Memory memory = includeResource("cast/scalar.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testArray(){
        Memory memory = includeResource("cast/array.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testObject(){
        Memory memory = includeResource("cast/object.php");
        Assert.assertEquals("success", memory.toString());
    }
}
