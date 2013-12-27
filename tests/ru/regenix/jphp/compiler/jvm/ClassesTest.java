package ru.regenix.jphp.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import ru.regenix.jphp.runtime.lang.IObject;
import ru.regenix.jphp.runtime.memory.ObjectMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;

import java.lang.reflect.InvocationTargetException;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClassesTest extends JvmCompilerCase {

    @Test
    public void testDefine(){
        Memory memory;
        memory = runDynamic("class A { } return new A();", false);
        Assert.assertTrue(memory.isObject());

        IObject object = ((ObjectMemory)memory).value;
        Assert.assertEquals("A", object.getReflection().getName());
    }

    @Test
    public void testProperties() throws InvocationTargetException, IllegalAccessException {
        Memory memory;
        memory = runDynamic("class A { var $x, $y = 30; } return new A();", false);
        Assert.assertTrue(memory.isObject());

        IObject object = ((ObjectMemory)memory).value;
        Assert.assertEquals(30, object.getReflection().getProperty(environment, null, object, "x").toLong());
        Assert.assertEquals(30, object.getReflection().getProperty(environment, null, object, "y").toLong());

        memory = runDynamic("class A { public $arr = array(1, 2, 3); } return new A()->arr;", false);
        Assert.assertTrue(memory.isArray());
    }

    @Test
    public void testMethods(){
        Memory memory;
        memory = runDynamic("class A { function b() { return 'foobar'; } } return new A()->b();", false);
        Assert.assertTrue(memory.isString());
        Assert.assertEquals("foobar", memory.toString());

        memory = runDynamic("class A { static function b() { return 100500; } } return A::b();", false);
        Assert.assertEquals(100500, memory.toLong());

        memory = runDynamic("class A { static function b() { return 100500; } } return new A->b();", false);
        Assert.assertEquals(100500, memory.toLong());
    }

    @Test
    public void testSimple(){
        Memory memory = includeResource("classes/simple.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testStdClass(){
        Memory memory = includeResource("classes/std_class.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testInstanceOf(){
        Memory memory = includeResource("classes/instance_of.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testSelf(){
        Memory memory = includeResource("classes/self.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testStatic(){
        Memory memory = includeResource("classes/static.php");
        Assert.assertEquals("success", memory.toString());
    }
}
