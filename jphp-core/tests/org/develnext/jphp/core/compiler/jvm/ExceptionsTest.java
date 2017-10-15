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
public class ExceptionsTest extends JvmCompilerCase {

    @Test
    public void testSimple(){
        Memory memory = includeResource("exceptions/simple.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testTrace(){
        Memory memory = includeResource("exceptions/test_trace.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testUncaught(){
        includeResource("exceptions/uncaught.php");
    }

    @Test
    public void testFinally(){
        check("exceptions/finally.php");
    }

    @Test
    public void testFinallyReturn(){
        check("exceptions/finally_return.php");
        check("exceptions/finally_return2.php");
    }

    @Test
    public void testFinallyCatch(){
        check("exceptions/finally_catch.php");
        check("exceptions/finally_catch2.php");
        check("exceptions/finally_catch3.php");
    }

    @Test
    public void testFinallyMisc(){
        check("exceptions/finally_misc.php");
    }

    @Test
    public void testGetPrevious() {
        check("exceptions/get_previous.php");
    }

    @Test
    public void testErrors() {
        check("exceptions/divzero_1.php");
        check("exceptions/divzero_2.php", true);
        check("exceptions/shrshl_neg_1.php");
    }

    @Test
    public void testMultiple() {
        check("exceptions/multiple.php");
    }
}
