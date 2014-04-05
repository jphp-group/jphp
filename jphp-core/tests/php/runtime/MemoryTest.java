package php.runtime;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.memory.*;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MemoryTest {

    @Test
    public void testNull(){
        Memory memory = Memory.NULL;

        Assert.assertEquals(Memory.Type.NULL, memory.type);
        Assert.assertFalse(memory.toBoolean());
        Assert.assertEquals("", memory.toString());
        Assert.assertEquals(0.0, memory.toDouble(), 0.000001);
        Assert.assertEquals(0, memory.toLong());

        Assert.assertNotNull(memory.toNumeric());
        Assert.assertEquals(Memory.Type.INT, memory.toNumeric().type);
        Assert.assertEquals(0, memory.toNumeric().toLong());

        Assert.assertEquals(memory, memory.toImmutable());
        Assert.assertTrue(memory.isImmutable());
    }

    @Test
    public void testFalse(){
        Memory memory = Memory.FALSE;

        Assert.assertEquals(Memory.Type.BOOL, memory.type);
        Assert.assertFalse(memory.toBoolean());
        Assert.assertEquals("", memory.toString());
        Assert.assertEquals(0.0, memory.toDouble(), 0.000001);
        Assert.assertEquals(0, memory.toLong());

        Assert.assertNotNull(memory.toNumeric());
        Assert.assertEquals(Memory.Type.INT, memory.toNumeric().type);
        Assert.assertEquals(0, memory.toNumeric().toLong());

        Assert.assertEquals(memory, memory.toImmutable());
        Assert.assertTrue(memory.isImmutable());
    }

    @Test
    public void testTrue(){
        Memory memory = Memory.TRUE;

        Assert.assertEquals(Memory.Type.BOOL, memory.type);
        Assert.assertTrue(memory.toBoolean());
        Assert.assertEquals("1", memory.toString());
        Assert.assertEquals(1.0, memory.toDouble(), 0.000001);
        Assert.assertEquals(1, memory.toLong());

        Assert.assertNotNull(memory.toNumeric());
        Assert.assertEquals(Memory.Type.INT, memory.toNumeric().type);
        Assert.assertEquals(1, memory.toNumeric().toLong());

        Assert.assertEquals(memory, memory.toImmutable());
        Assert.assertTrue(memory.isImmutable());
    }

    @Test
    public void testLong(){
        LongMemory memory = new LongMemory(100);

        Assert.assertEquals(Memory.Type.INT, memory.type);
        Assert.assertTrue(memory.toBoolean());
        Assert.assertEquals("100", memory.toString());
        Assert.assertEquals(100, memory.toDouble(), 0.000001);
        Assert.assertEquals(100, memory.toLong());

        Assert.assertNotNull(memory.toNumeric());
        Assert.assertEquals(Memory.Type.INT, memory.toNumeric().type);
        Assert.assertEquals(100, memory.toNumeric().toLong());

        Assert.assertEquals(memory, memory.toImmutable());
        Assert.assertTrue(memory.isImmutable());
    }

    @Test
    public void testDouble(){
        DoubleMemory memory = new DoubleMemory(50);

        Assert.assertEquals(Memory.Type.DOUBLE, memory.type);
        Assert.assertTrue(memory.toBoolean());
        Assert.assertEquals("50", memory.toString());
        Assert.assertEquals(50, memory.toDouble(), 0.000001);
        Assert.assertEquals(50, memory.toLong());

        Assert.assertNotNull(memory.toNumeric());
        Assert.assertEquals(Memory.Type.DOUBLE, memory.toNumeric().type);
        Assert.assertEquals(50, memory.toNumeric().toLong());

        Assert.assertEquals(memory, memory.toImmutable());
        Assert.assertTrue(memory.isImmutable());
    }

    @Test
    public void testString(){
        StringMemory memory = new StringMemory("foobar");

        Assert.assertEquals(Memory.Type.STRING, memory.type);
        Assert.assertTrue(memory.toBoolean());
        Assert.assertEquals("foobar", memory.toString());
        Assert.assertEquals(0.0, memory.toDouble(), 0.000001);
        Assert.assertEquals(0, memory.toLong());

        Assert.assertNotNull(memory.toNumeric());
        Assert.assertEquals(Memory.Type.INT, memory.toNumeric().type);
        Assert.assertEquals(0, memory.toNumeric().toLong());

        Assert.assertEquals(memory, memory.toImmutable());
        Assert.assertTrue(memory.isImmutable());

        Assert.assertNull(StringMemory.toLong("-"));
        Assert.assertEquals(-1, StringMemory.toLong("-1").toLong());
        Assert.assertEquals(32, StringMemory.toLong("32").toLong());
    }

    @Test
    public void testReference(){
        ReferenceMemory memory = new ReferenceMemory(Memory.TRUE);

        Assert.assertEquals(Memory.Type.REFERENCE, memory.type);
        Assert.assertTrue(memory.toBoolean());
        Assert.assertEquals("1", memory.toString());
        Assert.assertEquals(1, memory.toDouble(), 0.000001);
        Assert.assertEquals(1, memory.toLong());

        Assert.assertNotNull(memory.toNumeric());
        Assert.assertEquals(Memory.Type.INT, memory.toNumeric().type);
        Assert.assertEquals(1, memory.toNumeric().toLong());

        Assert.assertNotEquals(memory, memory.toImmutable());
        Assert.assertEquals(Memory.TRUE, memory.toImmutable());
        Assert.assertFalse(memory.isImmutable());
    }
}
