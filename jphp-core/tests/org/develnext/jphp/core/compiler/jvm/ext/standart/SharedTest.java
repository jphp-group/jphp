package org.develnext.jphp.core.compiler.jvm.ext.standart;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SharedTest extends JvmCompilerCase {

    @Test
    public void testValue() {
        check("ext/standard/shared/value_001.php");
        check("ext/standard/shared/value_002.php");
    }

    @Test
    public void testBasic() {
        check("ext/standard/shared/basic_001.php");
        check("ext/standard/shared/basic_002.php");
    }

    @Test
    public void testMap() {
        check("ext/standard/shared/map_001.php");
    }

    @Test
    public void testStack() {
        check("ext/standard/shared/stack_001.php");
    }

    @Test
    public void testQueue() {
        check("ext/standard/shared/queue_001.php");
    }
}
