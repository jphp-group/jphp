package ru.regenix.jphp.runtime.memory;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import ru.regenix.jphp.runtime.memory.support.Memory;

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
        ArrayMemory memory = new ArrayMemory();
        assertTrue(memory.refOfIndex(0) instanceof ReferenceMemory);
        assertEquals(Memory.NULL, memory.refOfIndex(0).toImmutable());
        memory.refOfIndex(0).assign(100500);
        assertEquals(100500, memory.refOfIndex(0).toLong());
        assertEquals(1, memory.size());

        memory.refOfIndex("0").assign(100);
        assertEquals(1, memory.size());
        assertEquals(100, memory.refOfIndex(0).toLong());

        memory.refOfIndex(false).assign(100500);
        assertEquals(100500, memory.refOfIndex(false).toLong());
        assertEquals(1, memory.size());

        memory.refOfIndex(true).assign(100);
        assertEquals(100, memory.refOfIndex(true).toLong());
        assertEquals(2, memory.size());

        memory.refOfIndex("foobar");
        assertEquals(3, memory.size());
        assertEquals(Memory.NULL, memory.refOfIndex("foobar").toImmutable());
        memory.refOfIndex("foobar").assign("bar");
        assertEquals("bar", memory.refOfIndex("foobar").toString());
    }

    @Test
    public void testValueOfIndex(){
        ArrayMemory memory = new ArrayMemory();
        assertEquals(Memory.NULL, memory.valueOfIndex(0));
        assertEquals(0, memory.size());

        memory.refOfIndex(new DoubleMemory(2)).assign(2);
        assertEquals(2, memory.valueOfIndex(2.0).toLong());
    }

    @Test
    public void testRemoveInList(){
        ArrayMemory memory = new ArrayMemory();
        memory.refOfIndex(0).assign(1);
        memory.refOfIndex(1).assign(2);
        memory.refOfIndex(2).assign(3);

        assertEquals(3, memory.size());
        memory.removeByScalar(1);
        assertEquals(2, memory.size());
        assertEquals(Memory.NULL, memory.valueOfIndex(1));

        memory.refOfIndex(1).assign(33);
        assertEquals(33, memory.valueOfIndex(1).toLong());
        assertEquals(3, memory.size());

        memory.removeByScalar(0);
        memory.removeByScalar("1");
        memory.removeByScalar(2L);
        memory.removeByScalar(100500);
        assertEquals(0, memory.size());

        assertEquals(Memory.NULL, memory.valueOfIndex(0));
        assertEquals(Memory.NULL, memory.valueOfIndex(2));
    }

    @Test
    public void testRemoveInMap(){
        ArrayMemory memory = new ArrayMemory();
        memory.refOfIndex("x1").assign("y1");
        memory.refOfIndex("x2").assign("y2");
        memory.refOfIndex("x3").assign("y3");

        assertEquals(3, memory.size());
        memory.removeByScalar("x1");
        assertEquals(Memory.NULL, memory.valueOfIndex("x1"));
        assertEquals(2, memory.size());

        memory.refOfIndex("x1").assign("foobar");
        assertEquals("foobar", memory.valueOfIndex("x1").toString());
        assertEquals(3, memory.size());

        memory.removeByScalar("x1");
        memory.removeByScalar("x2");
        memory.removeByScalar("x3");
    }

    @Test
    public void testImmutable(){
        ArrayMemory arr1 = new ArrayMemory();
        arr1.refOfIndex("x1").assign(1);
        arr1.refOfIndex("x2").assign(2);

        assertTrue(arr1.toImmutable() instanceof ArrayMemory);
        ArrayMemory arr2 = (ArrayMemory) arr1.toImmutable();
        assertEquals(2, arr1.copies);

        assertNotEquals(arr1, arr2);
        assertEquals(2, arr2.size());
        assertTrue(arr1.valueOfIndex("x1") == arr2.valueOfIndex("x1"));
        assertTrue(arr1.valueOfIndex("x2") == arr2.valueOfIndex("x2"));
        assertEquals(1, arr2.valueOfIndex("x1").toLong());
        assertEquals(2, arr2.valueOfIndex("x2").toLong());

        arr2.refOfIndex("x1").assign(100500);
        assertEquals(100500, arr2.valueOfIndex("x1").toLong());
        assertEquals(1, arr1.valueOfIndex("x1").toLong());
        assertTrue(arr1.valueOfIndex("x2") != arr2.valueOfIndex("x2"));

        assertEquals(1, arr1.copies);

        arr1 = (ArrayMemory) arr2.toImmutable();
        arr2.refOfIndex("x1").assign(100);
        assertEquals(100, arr2.valueOfIndex("x1").toLong());
        assertEquals(100500, arr1.valueOfIndex("x1").toLong());

        arr1 = (ArrayMemory) arr2.toImmutable();
        assertEquals(1, arr2.copies);
        assertEquals(0, arr1.copies);

        arr1.unset();
        assertEquals(0, arr2.copies);
        assertEquals(2, arr2.size());
        assertEquals(100, arr2.valueOfIndex("x1").toLong());

        arr1 = (ArrayMemory) arr2.toImmutable();
        arr2.unset();
        assertEquals(0, arr2.size());
        assertEquals(2, arr1.size());
        assertEquals(100, arr1.valueOfIndex("x1").toLong());

        arr1.unset();
        assertEquals(0, arr1.size());
    }

    @Test
    public void testMisc(){
        ArrayMemory arr = new ArrayMemory();
        assertEquals(0, arr.toLong());
        assertFalse(arr.toBoolean());

        arr.refOfIndex(1);
        arr.refOfIndex(2);
        assertEquals(1, arr.toLong());
        assertTrue(arr.toBoolean());
    }
}
