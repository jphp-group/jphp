package org.develnext.jphp.core.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.lang.IObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.Memory;

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
    public void testProperties() throws Throwable {
        Memory memory;
        memory = runDynamic("class A { var $x = 11, $y = 30; } return new A();", false);
        Assert.assertTrue(memory.isObject());

        IObject object = ((ObjectMemory)memory).value;
        Assert.assertEquals(11, object.getReflection().getProperty(environment, null, object, "x", null, 0).toLong());
        Assert.assertEquals(30, object.getReflection().getProperty(environment, null, object, "y", null, 0).toLong());

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

    @Test
    public void testToString(){
        Memory memory = includeResource("classes/__toString.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testDestruct(){
        Memory memory = includeResource("classes/__destruct.php");
        Assert.assertEquals("1111111111", getOutput());
    }

    @Test
    public void testCompare(){
        check("classes/compare.php");
    }

    @Test
    public void testClassNameConstant(){
        check("classes/class_name_constant.php");
    }

    @Test
    public void testNew() {
        Memory memory = includeResource("classes/new.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testStaticDynVars() {
        check("classes/static_dyn_vars.php");
    }

    @Test
    public void testStaticCallByVar() {
        check("classes/static_call_by_var.php");
    }

    @Test
    public void testAutoloadDuplicateClasses() {
        check("classes/autoload_duplicate_classes.php");
    }

    @Test
    public void testAbstractInheritance() {
        check("classes/abstract_inheritance_001.phpt", true);
        check("classes/abstract_inheritance_002.phpt", true);
        check("classes/abstract_inheritance_003.phpt", true);
    }

    @Test
    public void testAnonClasses() {
        check("classes/anon_classes_001.phpt", true);
        check("classes/anon_classes_002.phpt", true);
        check("classes/anon_classes_003.phpt", true);
        check("classes/anon_classes_004.phpt", true);
    }

    @Test
    public void testBugs() {
        check("classes/bug107.php");
        check("classes/bug123.php");
        check("classes/bug127.php");
        check("classes/bug130.php");
    }

    @Test
    public void testBug391() {
        check("classes/bug391.phpt", true);
    }

    @Test
    public void testBug404() {
        check("classes/bug404.phpt", true);
    }

    @Test
    public void testBug388() {
        check("classes/bug388.phpt", true);
    }

    @Test
    public void testBug264() {
        check("classes/bug264.php");
    }

    @Test
    public void testBug271() {
        check("classes/bug271.php");
    }
}
