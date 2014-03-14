package org.develnext.jphp.core.compiler.jvm.ext.core;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EnvironmentTest extends JvmCompilerCase {

    @Test
    public void testBasic(){
        check("ext/core/Environment_001.php");
        check("ext/core/Environment_002.php");
        check("ext/core/Environment_003.php");
        check("ext/core/Environment_004.php");
        check("ext/core/Environment_005.php");
        check("ext/core/Environment_006.php");
    }
}
