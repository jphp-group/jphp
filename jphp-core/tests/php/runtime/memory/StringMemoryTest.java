package php.runtime.memory;

import static org.assertj.core.api.Assertions.assertThat;
import static php.runtime.env.TraceInfo.UNKNOWN;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.Memory;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StringMemoryTest {

    @Test
    public void testToNumeric(){
        Memory memory = StringMemory.toNumeric("123");
        Assert.assertEquals(Memory.Type.INT, memory.type);
        Assert.assertEquals(123, memory.toLong());

        memory = StringMemory.toNumeric("-123");
        Assert.assertEquals(Memory.Type.INT, memory.type);
        Assert.assertEquals(-123, memory.toLong());

        memory = StringMemory.toNumeric("  123");
        Assert.assertEquals(Memory.Type.INT, memory.type);
        Assert.assertEquals(123, memory.toLong());

        memory = StringMemory.toNumeric("123   foobar");
        Assert.assertEquals(Memory.Type.INT, memory.type);
        Assert.assertEquals(123, memory.toLong());

        memory = StringMemory.toNumeric("123foobar");
        Assert.assertEquals(Memory.Type.INT, memory.type);
        Assert.assertEquals(123, memory.toLong());

        memory = StringMemory.toNumeric("123.foobar");
        Assert.assertEquals(Memory.Type.DOUBLE, memory.type);
        Assert.assertEquals(123.0, memory.toDouble(), 0.00001);

        memory = StringMemory.toNumeric("123.45");
        Assert.assertEquals(Memory.Type.DOUBLE, memory.type);
        Assert.assertEquals(123.45, memory.toDouble(), 0.00001);

        memory = StringMemory.toNumeric("\n\r   123.45  foobar");
        Assert.assertEquals(Memory.Type.DOUBLE, memory.type);
        Assert.assertEquals(123.45, memory.toDouble(), 0.00001);

        memory = StringMemory.toNumeric("123.45 foobar");
        Assert.assertEquals(Memory.Type.DOUBLE, memory.type);
        Assert.assertEquals(123.45, memory.toDouble(), 0.00001);

        memory = StringMemory.toNumeric("123.45foobar");
        Assert.assertEquals(Memory.Type.DOUBLE, memory.type);
        Assert.assertEquals(123.45, memory.toDouble(), 0.00001);
    }

    @Test
    public void testConcat(){
        StringMemory memory = new StringMemory("foo");
        Assert.assertEquals("foobar", memory.concat("bar"));
        Assert.assertEquals("foo123", memory.concat(123));
        Assert.assertEquals("foo123", memory.concat(123.0));
        Assert.assertEquals("foo1", memory.concat(true));
        Assert.assertEquals("foo", memory.concat(false));

        Assert.assertEquals("foo", memory.concat(Memory.NULL));
        Assert.assertEquals("foo", memory.concat(Memory.FALSE));
        Assert.assertEquals("foo1", memory.concat(Memory.TRUE));
        Assert.assertEquals("foo123", memory.concat(new LongMemory(123)));
        Assert.assertEquals("foo123", memory.concat(new DoubleMemory(123)));
        Assert.assertEquals("foobar", memory.concat(new StringMemory("bar")));
    }

    @Test
    public void testAppend(){
        StringBuilderMemory memory = new StringBuilderMemory("foo");
        memory.append("bar");
        memory.append(123);
        memory.append(true);
        memory.append(false);
        memory.append(123.0);

        Assert.assertEquals("foobar1231123.0", memory.toString());
        Assert.assertEquals("foobar1231123.0foobar", memory.concat("foobar"));
    }

    @Test
    public void testNegative(){
        StringMemory memory = new StringMemory("123");
        Assert.assertEquals(-123, memory.negative().toLong());
        Assert.assertEquals(Memory.Type.INT, memory.negative().type);

        memory = new StringMemory("-123");
        Assert.assertEquals(123, memory.negative().toLong());
        Assert.assertEquals(Memory.Type.INT, memory.negative().type);
    }

    @Test
    public void testInc(){
        StringMemory memory = new StringMemory("3");
        Assert.assertEquals(4, memory.inc().toLong());
        Assert.assertEquals(Memory.Type.INT, memory.inc().type);

        memory = new StringMemory("2.3");
        Assert.assertEquals(3.3, memory.inc().toDouble(), 0.000001);
        Assert.assertEquals(Memory.Type.DOUBLE, memory.inc().type);
    }

    @Test
    public void testPlus(){
        StringMemory memory = new StringMemory("10");

        Assert.assertEquals(Memory.Type.INT, memory.plus(2).type);
        Assert.assertEquals(12, memory.plus(2).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.plus(true).type);
        Assert.assertEquals(11, memory.plus(true).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.plus(false).type);
        Assert.assertEquals(10, memory.plus(false).toLong());

        Assert.assertEquals(Memory.Type.DOUBLE, memory.plus(2.0).type);
        Assert.assertEquals(12, memory.plus(2.0).toDouble(), 0.00001);

        Assert.assertEquals(Memory.Type.INT, memory.plus("2").type);
        Assert.assertEquals(12, memory.plus("2").toLong());

        Assert.assertEquals(Memory.Type.DOUBLE, memory.plus("2.0").type);
        Assert.assertEquals(12.0, memory.plus("2.0").toDouble(), 0.000001);


        Assert.assertEquals(Memory.Type.INT, memory.plus(Memory.NULL).type);
        Assert.assertEquals(10, memory.plus(Memory.NULL).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.plus(Memory.FALSE).type);
        Assert.assertEquals(10, memory.plus(Memory.FALSE).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.plus(Memory.TRUE).type);
        Assert.assertEquals(11, memory.plus(Memory.TRUE).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.plus(Memory.CONST_INT_2).type);
        Assert.assertEquals(12, memory.plus(Memory.CONST_INT_2).toLong());

        Assert.assertEquals(Memory.Type.DOUBLE, memory.plus(Memory.CONST_DOUBLE_1).type);
        Assert.assertEquals(11.0, memory.plus(Memory.CONST_DOUBLE_1).toDouble(), 0.000001);
    }

    @Test
    public void testMinus(){
        StringMemory memory = new StringMemory("10");

        Assert.assertEquals(Memory.Type.INT, memory.minus(2).type);
        Assert.assertEquals(8, memory.minus(2).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.minus(true).type);
        Assert.assertEquals(9, memory.minus(true).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.minus(false).type);
        Assert.assertEquals(10, memory.minus(false).toLong());

        Assert.assertEquals(Memory.Type.DOUBLE, memory.minus(2.0).type);
        Assert.assertEquals(8, memory.minus(2.0).toDouble(), 0.00001);

        Assert.assertEquals(Memory.Type.INT, memory.minus("2").type);
        Assert.assertEquals(8, memory.minus("2").toLong());

        Assert.assertEquals(Memory.Type.DOUBLE, memory.minus("2.0").type);
        Assert.assertEquals(8.0, memory.minus("2.0").toDouble(), 0.000001);


        Assert.assertEquals(Memory.Type.INT, memory.minus(Memory.NULL).type);
        Assert.assertEquals(10, memory.minus(Memory.NULL).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.minus(Memory.FALSE).type);
        Assert.assertEquals(10, memory.minus(Memory.FALSE).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.minus(Memory.TRUE).type);
        Assert.assertEquals(9, memory.minus(Memory.TRUE).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.minus(Memory.CONST_INT_2).type);
        Assert.assertEquals(8, memory.minus(Memory.CONST_INT_2).toLong());

        Assert.assertEquals(Memory.Type.DOUBLE, memory.minus(Memory.CONST_DOUBLE_1).type);
        Assert.assertEquals(9.0, memory.minus(Memory.CONST_DOUBLE_1).toDouble(), 0.000001);
    }

    @Test
    public void testMul(){
        StringMemory memory = new StringMemory("10");

        Assert.assertEquals(Memory.Type.INT, memory.mul(2).type);
        Assert.assertEquals(20, memory.mul(2).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.mul(true).type);
        Assert.assertEquals(10, memory.mul(true).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.mul(false).type);
        Assert.assertEquals(0, memory.mul(false).toLong());

        Assert.assertEquals(Memory.Type.DOUBLE, memory.mul(2.0).type);
        Assert.assertEquals(20.0, memory.mul(2.0).toDouble(), 0.00001);

        Assert.assertEquals(Memory.Type.INT, memory.mul("2").type);
        Assert.assertEquals(20, memory.mul("2").toLong());

        Assert.assertEquals(Memory.Type.DOUBLE, memory.mul("2.0").type);
        Assert.assertEquals(20, memory.mul("2.0").toDouble(), 0.000001);


        Assert.assertEquals(Memory.Type.INT, memory.mul(Memory.NULL).type);
        Assert.assertEquals(0, memory.mul(Memory.NULL).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.mul(Memory.FALSE).type);
        Assert.assertEquals(0, memory.mul(Memory.FALSE).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.mul(Memory.TRUE).type);
        Assert.assertEquals(10, memory.mul(Memory.TRUE).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.mul(Memory.CONST_INT_2).type);
        Assert.assertEquals(20, memory.mul(Memory.CONST_INT_2).toLong());

        Assert.assertEquals(Memory.Type.DOUBLE, memory.mul(Memory.CONST_DOUBLE_1).type);
        Assert.assertEquals(10.0, memory.mul(Memory.CONST_DOUBLE_1).toDouble(), 0.000001);
    }

    @Test
    public void testPow(){
        StringMemory memory = new StringMemory("10");

        Assert.assertEquals(Memory.Type.INT, memory.pow(2).type);
        Assert.assertEquals(100, memory.pow(2).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.pow(true).type);
        Assert.assertEquals(10, memory.pow(true).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.pow(false).type);
        Assert.assertEquals(1, memory.pow(false).toLong());

        Assert.assertEquals(Memory.Type.DOUBLE, memory.pow(2.0).type);
        Assert.assertEquals(100.0, memory.pow(2.0).toDouble(), 0.00001);

        Assert.assertEquals(Memory.Type.INT, memory.pow("2").type);
        Assert.assertEquals(100, memory.pow("2").toLong());

        Assert.assertEquals(Memory.Type.DOUBLE, memory.pow("2.0").type);
        Assert.assertEquals(100, memory.pow("2.0").toDouble(), 0.000001);


        Assert.assertEquals(Memory.Type.INT, memory.pow(Memory.NULL).type);
        Assert.assertEquals(1, memory.pow(Memory.NULL).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.pow(Memory.FALSE).type);
        Assert.assertEquals(1, memory.pow(Memory.FALSE).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.pow(Memory.TRUE).type);
        Assert.assertEquals(10, memory.pow(Memory.TRUE).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.pow(Memory.CONST_INT_2).type);
        Assert.assertEquals(100, memory.pow(Memory.CONST_INT_2).toLong());

        Assert.assertEquals(Memory.Type.DOUBLE, memory.pow(Memory.CONST_DOUBLE_1).type);
        Assert.assertEquals(10.0, memory.pow(Memory.CONST_DOUBLE_1).toDouble(), 0.000001);
    }

    @Test
    public void testDiv(){
        StringMemory memory = new StringMemory("10");

        Assert.assertEquals(Memory.Type.INT, memory.div(2).type);
        Assert.assertEquals(5, memory.div(2).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.div(true).type);
        Assert.assertEquals(10, memory.div(true).toLong());

        Assert.assertEquals(Memory.Type.DOUBLE, memory.div(2.0).type);
        Assert.assertEquals(5.0, memory.div(2.0).toDouble(), 0.00001);

        Assert.assertEquals(Memory.Type.INT, memory.div("2").type);
        Assert.assertEquals(5, memory.div("2").toLong());

        Assert.assertEquals(Memory.Type.DOUBLE, memory.div("2.0").type);
        Assert.assertEquals(5, memory.div("2.0").toDouble(), 0.000001);

        Assert.assertEquals(Memory.Type.INT, memory.div(Memory.TRUE).type);
        Assert.assertEquals(10, memory.div(Memory.TRUE).toLong());

        Assert.assertEquals(Memory.Type.INT, memory.div(Memory.CONST_INT_2).type);
        Assert.assertEquals(5, memory.div(Memory.CONST_INT_2).toLong());

        Assert.assertEquals(Memory.Type.DOUBLE, memory.div(Memory.CONST_DOUBLE_1).type);
        Assert.assertEquals(10.0, memory.div(Memory.CONST_DOUBLE_1).toDouble(), 0.000001);
    }

    @Test
    public void testDivInvalidFalse(){
        StringMemory memory = new StringMemory("10");
        Assert.assertEquals(Memory.FALSE, memory.div(false));
    }

    @Test
    public void testDivInvalidFalse2(){
        StringMemory memory = new StringMemory("10");
        Assert.assertEquals(Memory.FALSE, memory.div(Memory.FALSE));
    }

    @Test
    public void testDivInvalidNull(){
        StringMemory memory = new StringMemory("10");
        Assert.assertEquals(Memory.FALSE, memory.div(Memory.NULL));
    }

    @Test
    public void testEqual(){
        StringMemory memory = new StringMemory("foobar");
        Assert.assertTrue(memory.equal("foobar"));
        Assert.assertFalse(memory.notEqual("foobar"));
        Assert.assertTrue(memory.equal(new StringMemory("foobar")));
        Assert.assertFalse(memory.notEqual(new StringMemory("foobar")));

        Assert.assertTrue(memory.equal(true));
        Assert.assertFalse(memory.equal(false));
        Assert.assertFalse(memory.notEqual(true));
        Assert.assertTrue(memory.notEqual(false));

        Assert.assertFalse(memory.equal(1));
        Assert.assertTrue(memory.equal(0));

        Assert.assertFalse(memory.equal(1.0));
        Assert.assertTrue(memory.equal(0.0));
    }

    @Test
    public void testSmaller(){
        // for non numeric
        StringMemory memory = new StringMemory("invalid_num");
        Assert.assertFalse(memory.smaller("foobar"));
        Assert.assertFalse(memory.smallerEq("foobar"));
        Assert.assertFalse(memory.smaller(new StringMemory("foobar")));
        Assert.assertFalse(memory.smallerEq(new StringMemory("foobar")));
    }

    @Test
    public void testGreater(){
        // for non numeric
        StringMemory memory = new StringMemory("invalid_num");
        Assert.assertTrue(memory.greater("foobar"));
        Assert.assertTrue(memory.greaterEq("foobar"));
        Assert.assertTrue(memory.greater(new StringMemory("foobar")));
        Assert.assertTrue(memory.greaterEq(new StringMemory("foobar")));
    }

    @Test
    public void testIssetOfIndexEmptyString() {
        StringMemory memory = new StringMemory("");

        assertThat(memory.issetOfIndex(UNKNOWN, Memory.CONST_INT_1)).isEqualTo(Memory.FALSE);
        assertThat(memory.issetOfIndex(UNKNOWN, Memory.CONST_INT_0)).isEqualTo(Memory.FALSE);
        assertThat(memory.issetOfIndex(UNKNOWN, Memory.CONST_INT_M1)).isEqualTo(Memory.FALSE);
    }

    @Test
    public void testIssetOfIndexNegative() {
        StringMemory memory = new StringMemory("abc");

        assertThat(memory.issetOfIndex(UNKNOWN, Memory.CONST_INT_3)).isEqualTo(Memory.FALSE);
    }

    @Test
    public void testIssetOfIndexPositive() {
        StringMemory memory = new StringMemory("abc");

        assertThat(memory.issetOfIndex(UNKNOWN, Memory.CONST_INT_0)).isEqualTo(Memory.TRUE);
        assertThat(memory.issetOfIndex(UNKNOWN, Memory.CONST_INT_1)).isEqualTo(Memory.TRUE);
        assertThat(memory.issetOfIndex(UNKNOWN, Memory.CONST_INT_2)).isEqualTo(Memory.TRUE);
    }
}
