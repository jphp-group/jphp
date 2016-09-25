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
}
