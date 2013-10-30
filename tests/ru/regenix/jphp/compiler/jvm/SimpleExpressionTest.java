package ru.regenix.jphp.compiler.jvm;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SimpleExpressionTest extends JvmCompilerCase {

    @Test
    public void testSimple(){
        /*Memory memory = run("1");
        Assert.assertEquals(1, memory.toLong());
        */
    }
}
