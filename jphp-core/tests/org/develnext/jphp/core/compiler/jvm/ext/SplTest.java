package org.develnext.jphp.core.compiler.jvm.ext;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SplTest extends JvmCompilerCase {

    @Test
    public void testIteratorIterator() {
         check("ext/spl/IteratorIterator_001.php");
         check("ext/spl/IteratorIterator_002.php");
         check("ext/spl/IteratorIterator_003.php");
    }
}
