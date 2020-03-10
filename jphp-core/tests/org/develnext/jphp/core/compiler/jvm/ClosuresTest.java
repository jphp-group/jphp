package org.develnext.jphp.core.compiler.jvm;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import php.runtime.Memory;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClosuresTest extends JvmCompilerCase {

    @Test
    public void testSimple(){
        Memory memory = includeResource("closures/simple.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testReferences(){
        Memory memory = includeResource("closures/references.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testDynamic(){
        Memory memory = includeResource("closures/dynamic.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testStaticVars(){
        Memory memory = includeResource("closures/static_vars.php");
        Assert.assertEquals("success", memory.toString());
    }

    @Test
    public void testStaticClosures() {
        check("closures/static_closure.phpt", true);
    }

    @Test
    public void testBugs() {
        check("closures/bug138.php");
    }

    @Test
    public void testBug266() {
        check("closures/bug266.php");
    }

    @Test
    public void testBug370() {
        check("closures/bug370.phpt");
    }

    @Test
    public void testBug407() {
        check("closures/bug407.phpt");
    }
}
