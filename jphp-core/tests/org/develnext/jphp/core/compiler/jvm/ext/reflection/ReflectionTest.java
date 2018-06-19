package org.develnext.jphp.core.compiler.jvm.ext.reflection;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import org.develnext.jphp.core.compiler.jvm.JvmCompilerCase;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReflectionTest extends JvmCompilerCase {

    @Test
    public void testExtension(){
        check("ext/reflection/ReflectionExtension_001.php");
    }

    @Test
    public void testFunction(){
        check("ext/reflection/ReflectionFunction_001.php");
        check("ext/reflection/ReflectionFunction_002.php");
        check("ext/reflection/ReflectionFunction_003.php");
        check("ext/reflection/ReflectionFunction_004.php");
        check("ext/reflection/ReflectionFunction_005.php");
    }

    @Test
    public void testParameter(){
        check("ext/reflection/ReflectionParameter_001.php", true);
        check("ext/reflection/ReflectionParameter_002.php", true);
        check("ext/reflection/ReflectionParameter_003.php", true);
        check("ext/reflection/ReflectionParameter_004.php", true);
        check("ext/reflection/ReflectionParameter_005.php", true);
    }

    @Test
    public void testClass(){
        check("ext/reflection/ReflectionClass_001.php");
        check("ext/reflection/ReflectionClass_002.php");
        check("ext/reflection/ReflectionClass_003.php");
        check("ext/reflection/ReflectionClass_004.php");
        check("ext/reflection/ReflectionClass_005.php");
        check("ext/reflection/ReflectionClass_006.php");
        check("ext/reflection/ReflectionClass_007.php");
        check("ext/reflection/ReflectionClass_008.php");
        check("ext/reflection/ReflectionClass_009.php");
        check("ext/reflection/ReflectionClass_010.php");
        check("ext/reflection/ReflectionClass_011.php");
        check("ext/reflection/ReflectionClass_012.php");
        check("ext/reflection/ReflectionClass_013.php");
        check("ext/reflection/ReflectionClass_014.php");
        check("ext/reflection/ReflectionClass_015.php");
        check("ext/reflection/ReflectionClass_016.php");
        check("ext/reflection/ReflectionClass_017.php");
        check("ext/reflection/ReflectionClass_018.php");
        check("ext/reflection/ReflectionClass_019.php");
        check("ext/reflection/ReflectionClass_020.php");
        check("ext/reflection/ReflectionClass_021.php", true);
        check("ext/reflection/ReflectionClass_022.php");
        check("ext/reflection/ReflectionClass_023.php");
        check("ext/reflection/ReflectionClass_024.php");
        check("ext/reflection/ReflectionClass_025.php");
        check("ext/reflection/ReflectionClass_026.php");
    }

    @Test
    public void testProperty(){
        check("ext/reflection/ReflectionProperty_001.php");
        check("ext/reflection/ReflectionProperty_002.php");
        check("ext/reflection/ReflectionProperty_003.php");
        check("ext/reflection/ReflectionProperty_004.php");
        check("ext/reflection/ReflectionProperty_005.php");
    }

    @Test
    public void testMethod(){
        check("ext/reflection/ReflectionMethod_001.php");
        check("ext/reflection/ReflectionMethod_002.php");
        check("ext/reflection/ReflectionMethod_003.php");
        check("ext/reflection/ReflectionMethod_004.php");
    }

    @Test
    public void testMethodBug267(){
        check("ext/reflection/ReflectionMethod_bug267.php");
    }
}
