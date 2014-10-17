package php.runtime.memory;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.Memory;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DoubleMemoryTest {

    @Test
    public void testPlus(){
        DoubleMemory memory = new DoubleMemory(10);
        Assert.assertEquals(12, memory.plus(2).toDouble(), 0.0001);
        Assert.assertEquals(Memory.Type.DOUBLE, memory.plus(2).type);

        Assert.assertEquals(11, memory.plus(true).toDouble(), 0.00001);
        Assert.assertEquals(10, memory.plus(false).toDouble(), 0.00001);
        Assert.assertEquals(Memory.Type.DOUBLE, memory.plus(false).type);

        Assert.assertEquals(12, memory.plus(2.0).toDouble(), 0.0001);
        Assert.assertEquals(Memory.Type.DOUBLE, memory.plus(2.0).type);
    }

    @Test
    public void testMinus(){
        LongMemory memory = new LongMemory(10);
        Assert.assertEquals(8, memory.minus(2).toLong());
        Assert.assertEquals(Memory.Type.INT, memory.minus(2).type);

        Assert.assertEquals(9, memory.minus(true).toLong());
        Assert.assertEquals(10, memory.minus(false).toLong());
        Assert.assertEquals(Memory.Type.INT, memory.minus(false).type);

        Assert.assertEquals(8.0, memory.minus(2.0).toDouble(), 0.0001);
        Assert.assertEquals(Memory.Type.DOUBLE, memory.minus(2.0).type);
    }

    @Test
    public void testMul(){
        DoubleMemory memory = new DoubleMemory(10);
        Assert.assertEquals(20, memory.mul(2).toDouble(), 0.00001);
        Assert.assertEquals(Memory.Type.DOUBLE, memory.mul(2).type);

        Assert.assertEquals(10, memory.mul(true).toDouble(), 0.00001);
        Assert.assertEquals(0, memory.mul(false).toDouble(), 0.00001);
        Assert.assertEquals(Memory.Type.DOUBLE, memory.mul(false).type);

        Assert.assertEquals(20, memory.mul(2.0).toDouble(), 0.0001);
        Assert.assertEquals(Memory.Type.DOUBLE, memory.mul(2.0).type);
    }

    @Test
    public void testPow(){
        DoubleMemory memory = new DoubleMemory(10);
        Assert.assertEquals(100, memory.pow(2).toDouble(), 0.00001);
        Assert.assertEquals(Memory.Type.DOUBLE, memory.pow(2).type);

        Assert.assertEquals(10, memory.pow(true).toDouble(), 0.00001);
        Assert.assertEquals(1, memory.pow(false).toDouble(), 0.00001);
        Assert.assertEquals(Memory.Type.DOUBLE, memory.pow(false).type);

        Assert.assertEquals(100, memory.pow(2.0).toDouble(), 0.0001);
        Assert.assertEquals(Memory.Type.DOUBLE, memory.pow(2.0).type);
    }

    @Test
    public void testDiv(){
        DoubleMemory memory = new DoubleMemory(10);
        Assert.assertEquals(5, memory.div(2).toDouble(), 0.00001);
        Assert.assertEquals(Memory.Type.DOUBLE, memory.div(2).type);

        Assert.assertEquals(10, memory.div(true).toDouble(), 0.000001);
        Assert.assertEquals(Memory.Type.DOUBLE, memory.div(true).type);

        Assert.assertEquals(5, memory.div(2.0).toDouble(), 0.0001);
        Assert.assertEquals(Memory.Type.DOUBLE, memory.div(2.0).type);
    }


    @Test
    public void testDivInvalidFalse(){
        DoubleMemory memory = new DoubleMemory(10);
        Assert.assertEquals(Memory.FALSE, memory.div(false));
    }

    @Test
    public void testDivInvalidFalse2(){
        DoubleMemory memory = new DoubleMemory(10);
        Assert.assertEquals(Memory.FALSE, memory.div(Memory.FALSE));
    }

    @Test
    public void testDivInvalidNull(){
        DoubleMemory memory = new DoubleMemory(10);
        Assert.assertEquals(Memory.FALSE, memory.div(Memory.NULL));
    }
}
