package php.runtime.memory;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.support.MemoryUtils;

import java.util.Set;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ArrayMemoryTest {

    @Test
    public void testSimple(){
        ArrayMemory memory = new ArrayMemory();
        assertEquals(Memory.Type.ARRAY, memory.type);
        assertEquals(0, memory.size());
        assertEquals(-1, memory.lastLongIndex);

        Memory mem = ArrayMemory.valueOf();
        assertTrue(mem instanceof ArrayMemory);
        assertEquals(-1, ((ArrayMemory) mem).lastLongIndex);
        assertEquals(0, ((ArrayMemory) mem).size());
    }

    @Test
    public void testRefOfIndex(){
        Environment env = new Environment();
        ArrayMemory memory = new ArrayMemory();
        assertTrue(memory.refOfIndex(env, 0) instanceof ReferenceMemory);
        assertEquals(Memory.UNDEFINED, memory.refOfIndex(env, 0).toImmutable());
        memory.refOfIndex(env, 0).assign(100500);
        assertEquals(100500, memory.refOfIndex(env, 0).toLong());
        assertEquals(1, memory.size());

        memory.refOfIndex(env, TraceInfo.UNKNOWN, "0").assign(100);
        assertEquals(1, memory.size());
        assertEquals(100, memory.refOfIndex(env, 0).toLong());

        memory.refOfIndex(false).assign(100500);
        assertEquals(100500, memory.refOfIndex(false).toLong());
        assertEquals(1, memory.size());

        memory.refOfIndex(true).assign(100);
        assertEquals(100, memory.refOfIndex(true).toLong());
        assertEquals(2, memory.size());

        memory.refOfIndex(env, TraceInfo.UNKNOWN,"foobar");
        assertEquals(3, memory.size());
        assertEquals(Memory.UNDEFINED, memory.refOfIndex(env, TraceInfo.UNKNOWN,"foobar").toImmutable());
        memory.refOfIndex(env, TraceInfo.UNKNOWN,"foobar").assign("bar");
        assertEquals("bar", memory.refOfIndex(env, TraceInfo.UNKNOWN,"foobar").toString());
    }

    @Test
    public void testValueOfIndex(){
        Environment env = new Environment();
        ArrayMemory memory = new ArrayMemory();
        assertEquals(Memory.UNDEFINED, memory.valueOfIndex(env, 0));
        assertEquals(0, memory.size());

        memory.refOfIndex(env, TraceInfo.UNKNOWN, new DoubleMemory(2)).assign(2);
        assertEquals(2, memory.valueOfIndex(env, TraceInfo.UNKNOWN, 2.0).toLong());
    }

    @Test
    public void testRemoveInList(){
        Environment env = new Environment();
        ArrayMemory memory = new ArrayMemory();
        memory.refOfIndex(env, 0).assign(1);
        memory.refOfIndex(env, 1).assign(2);
        memory.refOfIndex(env, 2).assign(3);

        assertEquals(3, memory.size());
        memory.removeByScalar(1);
        assertEquals(2, memory.size());
        assertEquals(Memory.UNDEFINED, memory.valueOfIndex(env, 1));

        memory.refOfIndex(env, 1).assign(33);
        assertEquals(33, memory.valueOfIndex(env, 1).toLong());
        assertEquals(3, memory.size());

        memory.removeByScalar(0);
        memory.removeByScalar("1");
        memory.removeByScalar(2L);
        memory.removeByScalar(100500);
        assertEquals(0, memory.size());

        assertEquals(Memory.UNDEFINED, memory.valueOfIndex(env, 0));
        assertEquals(Memory.UNDEFINED, memory.valueOfIndex(env, 2));
    }

    @Test
    public void testRemoveInMap(){
        Environment env = new Environment();
        ArrayMemory memory = new ArrayMemory();
        memory.refOfIndex(env, TraceInfo.UNKNOWN,"x1").assign("y1");
        memory.refOfIndex(env, TraceInfo.UNKNOWN,"x2").assign("y2");
        memory.refOfIndex(env, TraceInfo.UNKNOWN,"x3").assign("y3");

        assertEquals(3, memory.size());
        memory.removeByScalar("x1");
        assertEquals(Memory.UNDEFINED, memory.valueOfIndex(env, "x1"));
        assertEquals(2, memory.size());

        memory.refOfIndex(env, TraceInfo.UNKNOWN,"x1").assign("foobar");
        assertEquals("foobar", memory.valueOfIndex(env, "x1").toString());
        assertEquals(3, memory.size());

        memory.removeByScalar("x1");
        memory.removeByScalar("x2");
        memory.removeByScalar("x3");
    }

    @Test
    public void testImmutable(){
        Environment env = new Environment();
        ArrayMemory arr1 = new ArrayMemory();
        arr1.refOfIndex(env, TraceInfo.UNKNOWN,"x1").assign(1);
        arr1.refOfIndex(env, TraceInfo.UNKNOWN,"x2").assign(2);

        assertTrue(arr1.toImmutable() instanceof ArrayMemory);
        ArrayMemory arr2 = (ArrayMemory) arr1.toImmutable();
        assertEquals(2, arr1.copies);

        assertNotEquals(arr1, arr2);
        assertEquals(2, arr2.size());
        assertTrue(arr1.valueOfIndex(env, "x1") == arr2.valueOfIndex(env, "x1"));
        assertTrue(arr1.valueOfIndex(env, "x2") == arr2.valueOfIndex(env, "x2"));
        assertEquals(1, arr2.valueOfIndex(env, "x1").toLong());
        assertEquals(2, arr2.valueOfIndex(env, "x2").toLong());

        arr2.refOfIndex(env, TraceInfo.UNKNOWN,"x1").assign(100500);
        assertEquals(100500, arr2.valueOfIndex(env, "x1").toLong());
        assertEquals(1, arr1.valueOfIndex(env, "x1").toLong());
        assertTrue(arr1.valueOfIndex(env, "x2") != arr2.valueOfIndex(env, "x2"));

        assertEquals(1, arr1.copies);

        arr1 = (ArrayMemory) arr2.toImmutable();
        arr2.refOfIndex(env, TraceInfo.UNKNOWN,"x1").assign(100);
        assertEquals(100, arr2.valueOfIndex(env, "x1").toLong());
        assertEquals(100500, arr1.valueOfIndex(env, "x1").toLong());

        arr1 = (ArrayMemory) arr2.toImmutable();
        assertEquals(1, arr2.copies);
        assertEquals(0, arr1.copies);

        arr1.unset();
        assertEquals(0, arr2.copies);
        assertEquals(2, arr2.size());
        assertEquals(100, arr2.valueOfIndex(env, "x1").toLong());

        arr1 = (ArrayMemory) arr2.toImmutable();
        arr2.unset();
        assertEquals(0, arr2.size());
        assertEquals(2, arr1.size());
        assertEquals(100, arr1.valueOfIndex(env, "x1").toLong());

        arr1.unset();
        assertEquals(0, arr1.size());
    }

    @Test
    public void testMisc(){
        Environment env = new Environment();
        ArrayMemory arr = new ArrayMemory();
        assertEquals(0, arr.toLong());
        assertFalse(arr.toBoolean());

        arr.refOfIndex(env, 1);
        arr.refOfIndex(env, 2);
        assertEquals(1, arr.toLong());
        assertTrue(arr.toBoolean());
    }

    @Test
    public void testContainsKey() {
        ArrayMemory arr = new ArrayMemory();
        assertFalse(arr.containsKey(0));
        arr.add("test");
        assertEquals(1, arr.size());
        assertTrue(arr.containsKey(0));
        assertTrue(arr.containsKey("0"));
        assertFalse(arr.containsKey(1));
        arr.put("k", MemoryUtils.valueOf("v"));
        assertEquals(2, arr.size());
        assertTrue(arr.containsKey("k"));
    }

    @Test
    public void testKeySet() {
        ArrayMemory arr = new ArrayMemory();
        arr.add("t1");
        arr.add("t2");
        Set<Object> set = arr.keySet();
        Object[] setArr = set.toArray();
        assertEquals(2, setArr.length);
        assertEquals(0, setArr[0]);
        assertEquals(1, setArr[1]);
        //convert into hashmap
        arr.put("3", MemoryUtils.valueOf("t3"));
        set = arr.keySet();
        setArr = set.toArray();
        assertEquals(3, setArr.length);
        assertEquals(0, ((Memory)setArr[0]).toInteger());
        assertEquals(1, ((Memory)setArr[1]).toInteger());
        assertEquals("3", setArr[2].toString());
    }
}
