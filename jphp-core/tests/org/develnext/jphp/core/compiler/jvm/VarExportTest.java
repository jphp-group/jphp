package org.develnext.jphp.core.compiler.jvm;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VarExportTest extends JvmCompilerCase {

    @Test
    public void testSetState(){
        check("var_export/__set_state001.php");
    }

    @Test
    public void testScalar(){
        check("var_export/scalar.php");
    }

    @Test
    public void testArrays(){
        check("var_export/arrays.php");
    }

    @Test
    public void testClosure(){
        check("var_export/closure.php");
    }
}
