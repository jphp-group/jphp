package ru.regenix.jphp.compiler.jvm.ext.reflection;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import ru.regenix.jphp.compiler.jvm.JvmCompilerCase;

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
    }
}
