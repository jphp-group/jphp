package org.develnext.jphp.core.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.Memory;
import php.runtime.exceptions.support.ErrorException;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConstantsTest extends JvmCompilerCase {

    @Test
    public void testCommon(){
        check("constants/constants_001.php");
    }

    @Test
    public void testAccessToClassConst(){
        check("constants/constants_002.phpt", true);
        check("constants/constants_003.phpt", true);
        check("constants/constants_004.phpt", true);
    }

    @Test
    public void testBug395() {
        check("constants/bug395.phpt", true);
    }

    @Test
    public void testBug383() {
        check("constants/bug383.phpt", true);
    }

    @Test
    public void testBug390() {
        check("constants/bug390.phpt", true);
    }
}
