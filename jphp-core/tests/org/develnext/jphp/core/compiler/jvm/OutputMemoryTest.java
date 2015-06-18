package org.develnext.jphp.core.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OutputMemoryTest extends JvmCompilerCase {
    @Test
    public void testVarDump() {
        includeResource("output_memory/var_dump.php");
        Assert.assertEquals(
                "int(2)\n" +
                "float(3.3)\n" +
                "string(6) \"foobar\"\n" +
                "bool(true)\n" +
                "bool(false)\n" +
                "NULL\n",
        getOutput());
    }

    @Test
    public void testPrintR() {
        includeResource("output_memory/print_r.php");
        Assert.assertEquals(
                "2" +
                "3.3" +
                "foobar" +
                "1" +
                "" +
                "",
                getOutput());
    }

    @Test
    public void testDebugInfo() {
        check("output_memory/debug_info.php");
    }
}
