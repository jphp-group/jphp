package org.develnext.jphp.core.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.Memory;

import java.net.URL;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CastTest extends JvmCompilerCase {

    @Test
    public void testScalar(){
       URL src = Thread.currentThread().getContextClassLoader().getResource(".");

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
