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
public class IncludeTest extends JvmCompilerCase {

    @Test
    public void testSimpleInclude(){
        Memory memory = includeResource("include/simple_include.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testOnce(){
        Memory memory = includeResource("include/once.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testRequire(){
        Memory memory = includeResource("include/require.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testRequireOnce(){
        Memory memory = includeResource("include/require_once.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test(expected = ErrorException.class)
    public void testRequireInvalid(){
        includeResource("include/require_invalid.php");
    }
}
