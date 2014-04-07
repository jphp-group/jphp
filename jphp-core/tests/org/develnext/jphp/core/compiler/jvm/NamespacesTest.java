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
public class NamespacesTest extends JvmCompilerCase {

    @Test
    public void testSimple(){
        Memory memory = includeResource("namespaces/simple.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testUse(){
        Memory memory = includeResource("namespaces/use.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testUseAs(){
        Memory memory = includeResource("namespaces/use_as.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testGlobals() {
        check("namespaces/globals.php");
    }
}
