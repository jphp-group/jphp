package ru.regenix.jphp.compiler.jvm.runtime.memory;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LongMemoryTest {

    @Test
    public void testPlus(){
        LongMemory memory = new LongMemory(10);
        Assert.assertEquals(12, memory.plus(2).toLong());
        Assert.assertEquals(Memory.Type.INT, memory.plus(2).type);

        Assert.assertEquals(11, memory.plus(true).toLong());
        Assert.assertEquals(10, memory.plus(false).toLong());
        Assert.assertEquals(Memory.Type.INT, memory.plus(false).type);

        Assert.assertEquals(12, memory.plus(2.0).toDouble(), 0.0001);
        Assert.assertEquals(Memory.Type.DOUBLE, memory.plus(2.0).type);

    }
}
