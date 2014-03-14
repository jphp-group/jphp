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
public class TypesComparisonsTest extends JvmCompilerCase {

    @Test
    public void testSimple(){
        Memory memory = includeResource("cheat_sheet.php");
        Assert.assertEquals("success", memory.toString());
    }
}
