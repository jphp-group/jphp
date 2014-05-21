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
public class TrueMemoryTest {

    @Test
    public void testBasic(){
        Assert.assertNotNull(Memory.TRUE);
        Assert.assertTrue(Memory.TRUE instanceof TrueMemory);
    }

    @Test
    public void testToNumeric(){
        Assert.assertTrue(Memory.TRUE.toNumeric() instanceof LongMemory);
        Assert.assertEquals(1, Memory.TRUE.toNumeric().toLong());
    }

    @Test
    public void testTyped(){
        Assert.assertTrue(Memory.TRUE.toBoolean());
        Assert.assertEquals("1", Memory.TRUE.toString());
        Assert.assertEquals(1.0, Memory.TRUE.toDouble(), 0.00001);
        Assert.assertEquals(Memory.TRUE, Memory.TRUE.toImmutable());
    }

    @Test
    public void testNegative(){
        Assert.assertEquals(-1, Memory.TRUE.negative().toLong());
        Assert.assertTrue(Memory.TRUE.negative() instanceof LongMemory);
    }

    @Test
    public void testConcat(){
        Assert.assertEquals("1foobar", Memory.TRUE.concat("foobar"));
        Assert.assertEquals("1foobar", Memory.TRUE.concat(new StringMemory("foobar")));
        Assert.assertEquals("19", Memory.TRUE.concat(9));
        Assert.assertEquals("19", Memory.TRUE.concat(new LongMemory(9)));
        Assert.assertEquals("11", Memory.TRUE.concat(true));
        Assert.assertEquals("11", Memory.TRUE.concat(Memory.TRUE));
        Assert.assertEquals("1", Memory.TRUE.concat(false));
        Assert.assertEquals("1", Memory.TRUE.concat(Memory.FALSE));
        Assert.assertEquals("11", Memory.TRUE.concat(1.0));

        Assert.assertEquals("foobar1", Memory.TRUE.concatRight("foobar"));
        Assert.assertEquals("11", Memory.TRUE.concatRight(true));
        Assert.assertEquals("1", Memory.TRUE.concatRight(false));
        Assert.assertEquals("331", Memory.TRUE.concatRight(33));
        Assert.assertEquals("22.01", Memory.TRUE.concatRight(22.0));
    }

    @Test
    public void testPlus(){
        Assert.assertEquals(2, Memory.TRUE.plus(Memory.CONST_INT_1).toLong());
        Assert.assertTrue(Memory.TRUE.plus(Memory.CONST_INT_1) instanceof LongMemory);

        Assert.assertEquals(2.0, Memory.TRUE.plus(Memory.CONST_DOUBLE_1).toDouble(), 0.00001);
        Assert.assertTrue(Memory.TRUE.plus(Memory.CONST_DOUBLE_1) instanceof DoubleMemory);

        Assert.assertEquals(2, Memory.TRUE.plus(Memory.TRUE).toLong());
        Assert.assertTrue(Memory.TRUE.plus(Memory.TRUE) instanceof LongMemory);

        Assert.assertEquals(1, Memory.TRUE.plus(Memory.FALSE).toLong());
        Assert.assertTrue(Memory.TRUE.plus(Memory.FALSE) instanceof LongMemory);

        Assert.assertEquals(2, Memory.TRUE.plus(true).toLong());
        Assert.assertTrue(Memory.TRUE.plus(true) instanceof LongMemory);

        Assert.assertEquals(1, Memory.TRUE.plus(false).toLong());
        Assert.assertTrue(Memory.TRUE.plus(false) instanceof LongMemory);

        Assert.assertEquals(34, Memory.TRUE.plus(33).toLong());
        Assert.assertTrue(Memory.TRUE.plus(33) instanceof LongMemory);

        Assert.assertEquals(3.5, Memory.TRUE.plus(2.5).toDouble(), 0.00001);
    }

    @Test
    public void testMinus(){
        Assert.assertEquals(0, Memory.TRUE.minus(Memory.CONST_INT_1).toLong());
        Assert.assertTrue(Memory.TRUE.minus(Memory.CONST_INT_1) instanceof LongMemory);

        Assert.assertEquals(0.0, Memory.TRUE.minus(Memory.CONST_DOUBLE_1).toDouble(), 0.00001);
        Assert.assertTrue(Memory.TRUE.minus(Memory.CONST_DOUBLE_1) instanceof DoubleMemory);

        Assert.assertEquals(0, Memory.TRUE.minus(Memory.TRUE).toLong());
        Assert.assertTrue(Memory.TRUE.minus(Memory.TRUE) instanceof LongMemory);

        Assert.assertEquals(1, Memory.TRUE.minus(Memory.FALSE).toLong());
        Assert.assertTrue(Memory.TRUE.minus(Memory.FALSE) instanceof LongMemory);

        Assert.assertEquals(0, Memory.TRUE.minus(true).toLong());
        Assert.assertTrue(Memory.TRUE.minus(true) instanceof LongMemory);

        Assert.assertEquals(1, Memory.TRUE.minus(false).toLong());
        Assert.assertTrue(Memory.TRUE.minus(false) instanceof LongMemory);

        Assert.assertEquals(-1, Memory.TRUE.minus(2).toLong());
        Assert.assertTrue(Memory.TRUE.minus(2) instanceof LongMemory);

        Assert.assertEquals(0.5, Memory.TRUE.minus(0.5).toDouble(), 0.00001);

        Assert.assertEquals(0, Memory.TRUE.minusRight(true).toLong());
        Assert.assertEquals(-1, Memory.TRUE.minusRight(false).toLong());
        Assert.assertEquals(1, Memory.TRUE.minusRight(2).toLong());
        Assert.assertEquals(1.0, Memory.TRUE.minusRight(2.0).toDouble(), 0.0000001);
        Assert.assertEquals(1.0, Memory.TRUE.minusRight("2.0").toDouble(), 0.0000001);
    }

    @Test
    public void testMul(){
        Assert.assertEquals(1, Memory.TRUE.mul(Memory.CONST_INT_1).toLong());
        Assert.assertTrue(Memory.TRUE.mul(Memory.CONST_INT_1) instanceof LongMemory);

        Assert.assertEquals(1.0, Memory.TRUE.mul(Memory.CONST_DOUBLE_1).toDouble(), 0.00001);
        Assert.assertTrue(Memory.TRUE.mul(Memory.CONST_DOUBLE_1) instanceof DoubleMemory);

        Assert.assertEquals(1, Memory.TRUE.mul(Memory.TRUE).toLong());
        Assert.assertTrue(Memory.TRUE.mul(Memory.TRUE) instanceof LongMemory);

        Assert.assertEquals(0, Memory.TRUE.mul(Memory.FALSE).toLong());
        Assert.assertTrue(Memory.TRUE.mul(Memory.FALSE) instanceof LongMemory);

        Assert.assertEquals(1, Memory.TRUE.mul(true).toLong());
        Assert.assertTrue(Memory.TRUE.mul(true) instanceof LongMemory);

        Assert.assertEquals(0, Memory.TRUE.mul(false).toLong());
        Assert.assertTrue(Memory.TRUE.mul(false) instanceof LongMemory);

        Assert.assertEquals(2, Memory.TRUE.mul(2).toLong());
        Assert.assertTrue(Memory.TRUE.mul(2) instanceof LongMemory);

        Assert.assertEquals(0.5, Memory.TRUE.mul(0.5).toDouble(), 0.00001);
    }

    @Test
    public void testDiv(){
        Assert.assertEquals(1, Memory.TRUE.div(Memory.CONST_INT_1).toLong());
        Assert.assertTrue(Memory.TRUE.div(Memory.CONST_INT_1) instanceof LongMemory);

        Assert.assertEquals(1.0, Memory.TRUE.div(Memory.CONST_DOUBLE_1).toDouble(), 0.00001);
        Assert.assertTrue(Memory.TRUE.div(Memory.CONST_DOUBLE_1) instanceof DoubleMemory);

        Assert.assertEquals(1, Memory.TRUE.div(Memory.TRUE).toLong());
        Assert.assertTrue(Memory.TRUE.div(Memory.TRUE) instanceof LongMemory);

        Assert.assertEquals(0, Memory.TRUE.div(Memory.FALSE).toLong());
        Assert.assertTrue(Memory.TRUE.div(Memory.FALSE) instanceof FalseMemory);

        Assert.assertEquals(1, Memory.TRUE.div(true).toLong());
        Assert.assertTrue(Memory.TRUE.div(true) instanceof LongMemory);

        Assert.assertEquals(0, Memory.TRUE.div(false).toLong());
        Assert.assertTrue(Memory.TRUE.div(false) instanceof FalseMemory);

        Assert.assertEquals(0.5, Memory.TRUE.div(2).toDouble(), 0.0000001);
        Assert.assertTrue(Memory.TRUE.div(2) instanceof DoubleMemory);

        Assert.assertEquals(2, Memory.TRUE.div(0.5).toDouble(), 0.00001);

        Assert.assertEquals(1, Memory.TRUE.divRight(true).toLong());
        Assert.assertEquals(0, Memory.TRUE.divRight(false).toLong());
        Assert.assertTrue(Memory.TRUE.divRight(false) instanceof LongMemory);
        Assert.assertEquals(1, Memory.TRUE.divRight(true).toLong());
        Assert.assertTrue(Memory.TRUE.divRight(true) instanceof LongMemory);
        Assert.assertTrue(Memory.TRUE.divRight(1) instanceof LongMemory);
        Assert.assertTrue(Memory.TRUE.divRight(1).toLong() == 1);
        Assert.assertTrue(Memory.TRUE.divRight(1.0) instanceof DoubleMemory);
        Assert.assertEquals(1, Memory.TRUE.divRight(1.0).toDouble(), 0.000001);
    }

    @Test
    public void testMod(){
        Assert.assertEquals(0, Memory.TRUE.mod(Memory.CONST_INT_1).toLong());
        Assert.assertTrue(Memory.TRUE.mod(Memory.CONST_INT_1) instanceof LongMemory);

        Assert.assertEquals(0, Memory.TRUE.mod(Memory.CONST_DOUBLE_1).toDouble(), 0.00001);
        Assert.assertTrue(Memory.TRUE.mod(Memory.CONST_DOUBLE_1) instanceof LongMemory);

        Assert.assertEquals(0, Memory.TRUE.mod(Memory.TRUE).toLong());
        Assert.assertTrue(Memory.TRUE.mod(Memory.TRUE) instanceof LongMemory);

        Assert.assertEquals(0, Memory.TRUE.mod(Memory.FALSE).toLong());
        Assert.assertTrue(Memory.TRUE.mod(Memory.FALSE) instanceof FalseMemory);

        Assert.assertEquals(0, Memory.TRUE.mod(true).toLong());
        Assert.assertTrue(Memory.TRUE.mod(true) instanceof LongMemory);

        Assert.assertEquals(0, Memory.TRUE.mod(false).toLong());
        Assert.assertTrue(Memory.TRUE.mod(false) instanceof FalseMemory);

        Assert.assertEquals(1, Memory.TRUE.mod(2).toLong());
        Assert.assertTrue(Memory.TRUE.mod(2) instanceof LongMemory);

        Assert.assertTrue(Memory.TRUE.mod(0.5) instanceof FalseMemory);

        Assert.assertEquals(0, Memory.TRUE.modRight(true).toLong());
        Assert.assertTrue(Memory.TRUE.modRight(false).toLong() == 0);
        Assert.assertTrue(Memory.TRUE.modRight(false) instanceof LongMemory);
        Assert.assertEquals(0, Memory.TRUE.modRight(true).toLong());
        Assert.assertTrue(Memory.TRUE.modRight(true) instanceof LongMemory);
        Assert.assertTrue(Memory.TRUE.modRight(1) instanceof LongMemory);
        Assert.assertTrue(Memory.TRUE.modRight(1).toLong() == 0);
        Assert.assertTrue(Memory.TRUE.modRight(1.0) instanceof LongMemory);
        Assert.assertEquals(0, Memory.TRUE.modRight(1.3).toDouble(), 0.000001);
    }

    @Test
    public void testPow(){
        Assert.assertEquals(1, Memory.TRUE.pow(Memory.CONST_INT_1).toLong());
        Assert.assertTrue(Memory.TRUE.pow(Memory.CONST_INT_1) instanceof LongMemory);

        Assert.assertEquals(1, Memory.TRUE.pow(Memory.TRUE).toLong());
        Assert.assertTrue(Memory.TRUE.pow(Memory.TRUE) instanceof LongMemory);

        Assert.assertEquals(1, Memory.TRUE.pow(Memory.FALSE).toLong());
        Assert.assertTrue(Memory.TRUE.pow(Memory.FALSE) instanceof LongMemory);

        Assert.assertEquals(1, Memory.TRUE.pow(2).toLong());
        Assert.assertTrue(Memory.TRUE.pow(2) instanceof LongMemory);

        Assert.assertEquals(1, Memory.TRUE.powRight(true).toLong());
        Assert.assertTrue(Memory.TRUE.powRight(false).toLong() == 0);
        Assert.assertTrue(Memory.TRUE.powRight(false) instanceof LongMemory);
        Assert.assertEquals(1, Memory.TRUE.powRight(true).toLong());
        Assert.assertTrue(Memory.TRUE.powRight(true) instanceof LongMemory);
        Assert.assertTrue(Memory.TRUE.powRight(1) instanceof LongMemory);
        Assert.assertTrue(Memory.TRUE.powRight(1).toLong() == 1);
        Assert.assertTrue(Memory.TRUE.powRight(1.0) instanceof DoubleMemory);
        Assert.assertEquals(1.3, Memory.TRUE.powRight(1.3).toDouble(), 0.000001);
    }
}
