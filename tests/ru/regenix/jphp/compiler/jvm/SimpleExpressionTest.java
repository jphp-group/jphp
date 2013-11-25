package ru.regenix.jphp.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import ru.regenix.jphp.runtime.memory.*;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SimpleExpressionTest extends JvmCompilerCase {

    @Test
    public void testReturn(){
        Memory memory = run("return;", false);
        Assert.assertTrue(memory instanceof NullMemory);
    }

    @Test
    public void testSimple(){
        Memory memory = run("1");
        Assert.assertEquals(1, memory.toLong());
        Assert.assertTrue(memory instanceof LongMemory);

        memory = run("1.2");
        Assert.assertEquals(1.2, memory.toDouble(), 0.00001);

        memory = run("'foobar'");
        Assert.assertEquals("foobar", memory.toString());

        memory = run("false");
        Assert.assertTrue(memory instanceof FalseMemory);

        memory = run("true");
        Assert.assertTrue(memory instanceof TrueMemory);

        memory = run("null");
        Assert.assertTrue(memory instanceof NullMemory);
    }

    @Test
    public void testArithmetic(){
        Memory memory = run("1 + 2");
        Assert.assertEquals(3, memory.toLong());
        Assert.assertTrue(memory instanceof LongMemory);

        memory = run("1 - 2");
        Assert.assertEquals(-1, memory.toLong());
        Assert.assertTrue(memory instanceof LongMemory);

        memory = run("-1 - 2");
        Assert.assertEquals(-3, memory.toLong());
        Assert.assertTrue(memory instanceof LongMemory);

        memory = run("-1 * 2");
        Assert.assertEquals(-2, memory.toLong());
        Assert.assertTrue(memory instanceof LongMemory);

        memory = run("-1 / 2");
        Assert.assertEquals(-0.5, memory.toDouble(), 0.0000001);
        Assert.assertTrue(memory instanceof DoubleMemory);

        memory = run("5 % 2");
        Assert.assertEquals(1, memory.toLong());
        Assert.assertTrue(memory instanceof LongMemory);
    }

    @Test
    public void testConcat(){
        Memory memory = run("'foo' . 'bar'");
        Assert.assertEquals("foobar", memory.toString());

        memory = run("1 . 'bar'");
        Assert.assertEquals("1bar", memory.toString());

        memory = run("false . 'bar'");
        Assert.assertEquals("bar", memory.toString());

        memory = run("1.0 . 'bar'");
        Assert.assertEquals("1.0bar", memory.toString());
    }

    @Test
    public void testLocalVariable(){
        Memory memory = run("$x");
        Assert.assertTrue(memory instanceof NullMemory);
    }

    @Test
    public void testNot(){
        Memory memory = run("!true");
        Assert.assertEquals(false, memory.toBoolean());

        memory = runDynamic("!$x");
        Assert.assertEquals(true, memory.toBoolean());
    }

    @Test
    public void testAssign(){
        Memory memory = run("$x = 20");
        Assert.assertEquals(20, memory.toLong());
        Assert.assertTrue(memory instanceof LongMemory);
    }

    @Test
    public void testAssignVariable(){
        Memory memory = run("$x = 20; return $x", false);
        Assert.assertEquals(20, memory.toLong());
        Assert.assertTrue(memory instanceof LongMemory);
    }

    @Test
    public void testAssignConcat(){
        Memory memory = run("$x = 'foo'; $x .= 'bar'; return $x", false);
        Assert.assertEquals("foobar", memory.toString());
    }

    @Test
    public void testAssignPlus(){
        Memory memory = run("$x = 10; $x += 2; return $x", false);
        Assert.assertEquals(12, memory.toLong());
    }

    @Test
    public void testAssignMinus(){
        Memory memory = run("$x = 10; $x -= 2; return $x", false);
        Assert.assertEquals(8, memory.toLong());
    }

    @Test
    public void testAssignMul(){
        Memory memory = run("$x = 10; $x *= 2; return $x", false);
        Assert.assertEquals(20, memory.toLong());
    }

    @Test
    public void testAssignDiv(){
        Memory memory = run("$x = 10; $x /= 2; return $x", false);
        Assert.assertEquals(5, memory.toLong());
    }

    @Test
    public void testAssignMod(){
        Memory memory = run("$x = 10; $x %= 3; return $x", false);
        Assert.assertEquals(1, memory.toLong());
    }

    @Test
    public void testOr(){
        Memory memory = runDynamic("$x || true");
        Assert.assertEquals(true, memory.toBoolean());

        memory = runDynamic("$x or true");
        Assert.assertEquals(true, memory.toBoolean());
    }

    @Test
    public void testAnd(){
        Memory memory = runDynamic("$x && true");
        Assert.assertEquals(false, memory.toBoolean());

        memory = runDynamic("$x and true");
        Assert.assertEquals(false, memory.toBoolean());
    }

    @Test
    public void testAndOrComplex(){
        Memory memory = runDynamic("$x || (!$y && !false || true)");
        Assert.assertEquals(true, memory.toBoolean());

        memory = runDynamic("$x or (!$y and !false)");
        Assert.assertEquals(true, memory.toBoolean());

        memory = runDynamic("!$x && ($y || true)");
        Assert.assertEquals(true, memory.toBoolean());

        memory = runDynamic("!$x and ($y or true)");
        Assert.assertEquals(true, memory.toBoolean());

        memory = runDynamic("!($x && $y) && true || $z");
        Assert.assertEquals(true, memory.toBoolean());

        memory = runDynamic("$x || !$y && true");
        Assert.assertEquals(true, memory.toBoolean());

        memory = runDynamic("$x || (!$y && !null && 100500)");
        Assert.assertEquals(true, memory.toBoolean());

        memory = runDynamic("$z = $x || 22; return $z", false);
        Assert.assertEquals(22, memory.toLong());
    }

    @Test
    public void testMathComplex(){
        Memory memory = runDynamic("3 + $x * 4");
        Assert.assertEquals(3, memory.toLong());

        memory = runDynamic("(3 + $x * 4) / 2");
        Assert.assertEquals(1.5, memory.toDouble(), 0.000001);

        memory = runDynamic("$y = 2; return (7 + $x * 4) / $y;", false);
        Assert.assertEquals(3.5, memory.toDouble(), 0.000001);

        memory = runDynamic("$y = 2; return (8 + $x * 4) / $y * 2;", false);
        Assert.assertEquals(8, memory.toLong());
        Assert.assertEquals(Memory.Type.INT, memory.type);
    }
}
