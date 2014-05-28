package org.develnext.jphp.core.compiler.jvm;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MiscTest extends JvmCompilerCase {

    @Test
    public void testBugs(){
        check("misc/bug132.php");
        check("misc/bug134.php");
        check("misc/bug126.php");
    }
}
