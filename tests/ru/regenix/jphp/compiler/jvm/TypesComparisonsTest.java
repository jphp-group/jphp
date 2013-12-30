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
public class TypesComparisonsTest extends JvmCompilerCase {

    @Test
    public void testSimple(){
        Memory memory = includeResource("cheat_sheet.php");
        Assert.assertEquals("success", memory.toString());
    }
}
