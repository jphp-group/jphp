package org.develnext.jphp.core.compiler.jvm.ext.standart;

import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModuleTest extends JvmCompilerCase {

    @Test
    public void testBasic() {
        check("ext/standard/module/basic_001.php");
        check("ext/standard/module/basic_002.php");
        check("ext/standard/module/basic_003.php");
    }
}
