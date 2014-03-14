package org.develnext.jphp.core.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.exceptions.support.ErrorException;
import php.runtime.Memory;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EvalTest extends JvmCompilerCase {

    @Test
    public void testSimple(){
        Memory memory = includeResource("eval/simple.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testLocalScope(){
        Memory memory = includeResource("eval/local_scope.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testInvalid(){
        Memory memory = includeResource("eval/invalid.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test(expected = ErrorException.class)
    public void testError(){
        Memory memory = includeResource("eval/error.php");
        Assert.assertEquals("success", memory.toString());
    }
}
