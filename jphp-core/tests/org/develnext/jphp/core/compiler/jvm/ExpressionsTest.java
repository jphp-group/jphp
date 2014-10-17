package org.develnext.jphp.core.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.Memory;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExpressionsTest extends JvmCompilerCase {

    @Test
    public void testAssignAssign(){
        Memory memory = includeResource("expressions/assign_assign.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testPlusMinusMulDiv(){
        Memory memory = includeResource("expressions/plus_minus_mul_div.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testPow() {
        check("expressions/pow_variation_t_pow.php", true);
        check("expressions/pow_variation_pow.php", true);
    }

    @Test
    public void testComplexAssign(){
        Memory memory = includeResource("expressions/complex_assign.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testComplex(){
        Memory memory = includeResource("expressions/complex.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testIncDec(){
        Memory memory = includeResource("expressions/inc_dec.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testConstantExpressions() {
        check("expressions/constant_expressions.php");
    }
}
