package org.develnext.jphp.core.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.memory.ArrayMemory;
import php.runtime.Memory;

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

    @Test
    public void testArrayReference(){
        Memory memory = includeResource("arrays/array_reference.php");
        Assert.assertEquals(100, memory.toLong());
    }

    @Test
    public void testArrayCopy(){
        Memory memory = includeResource("arrays/array_copy.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testComplex(){
        Memory memory = includeResource("arrays/complex.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testArrayPushRefAssign(){
        Memory memory = includeResource("arrays/array_push_ref_assign.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testCompare(){
        check("arrays/compare.php");
    }

    @Test
    public void testPlus(){
        check("arrays/plus.php");
    }

    @Test
    public void testArrayReturn() {
        Memory memory = runDynamic("$baseDir = 'foobar_'; return array(" +
                "    'a' => $baseDir . 'bla',);", false);

        Assert.assertTrue(memory.isArray());
        Assert.assertEquals("foobar_bla", memory.valueOfIndex("a").toString());
        Assert.assertEquals(1, memory.toValue(ArrayMemory.class).size());
    }
}
