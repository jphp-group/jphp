package ru.regenix.jphp.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ArraysTest extends JvmCompilerCase {

    @Test
    public void testClassicDefine(){
        Memory memory = includeResource("arrays/classic_define.php");
        Assert.assertTrue(memory.isArray());

        ArrayMemory array = memory.toValue(ArrayMemory.class);
        Assert.assertEquals(3, array.size());
        Assert.assertEquals(1, array.valueOfIndex(0).toLong());
        Assert.assertEquals(2, array.valueOfIndex(1).toLong());
        Assert.assertEquals(3, array.valueOfIndex(2).toLong());
    }

    @Test
    public void testShortDefine(){
        Memory memory = includeResource("arrays/short_define.php");
        Assert.assertTrue(memory.isArray());

        ArrayMemory array = memory.toValue(ArrayMemory.class);
        Assert.assertEquals(3, array.size());
        Assert.assertEquals(1, array.valueOfIndex(0).toLong());
        Assert.assertEquals(2, array.valueOfIndex(1).toLong());
        Assert.assertEquals(3, array.valueOfIndex(2).toLong());
    }

    @Test
    public void testKeyvalueDefine(){
        Memory memory = includeResource("arrays/keyvalue_define.php");
        Assert.assertEquals(330, memory.toLong());
    }

    @Test
    public void testNullDefine(){
        Memory memory = includeResource("arrays/null_define.php");
        Assert.assertEquals(50, memory.toLong());
    }
}
