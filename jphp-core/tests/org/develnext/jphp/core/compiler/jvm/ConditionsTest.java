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
public class ConditionsTest extends JvmCompilerCase {

    @Test
    public void testIfElseIf(){
        Memory memory = includeResource("conditions/if_else_if.php");
        Assert.assertEquals("foobar", memory.toString());
    }

    @Test
    public void testSwitchCase(){
        Memory memory = includeResource("conditions/switch_case.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testEndswitch(){
        Memory memory = includeResource("conditions/endswitch.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testEndif(){
        Memory memory = includeResource("conditions/endif.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testNested(){
        Memory memory = includeResource("conditions/nested.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testElvis(){
        Memory memory = includeResource("conditions/elvis.php");
        Assert.assertEquals("success", memory.toString());
    }
}
