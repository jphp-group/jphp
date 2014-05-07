package scripting;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import php.runtime.Information;

import javax.script.*;


@RunWith(JUnit4.class)
public class JSR223Test {

    @Test
    public void testBase() {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("jphp");
        ScriptEngineFactory f = engine.getFactory();
        Assert.assertNotNull(engine);
        Assert.assertTrue(engine instanceof Compilable);
        Assert.assertNotNull(f);
        Assert.assertEquals(Information.NAME, f.getEngineName());
        Assert.assertEquals("php", f.getLanguageName());
    }

    @Test
    public void testVariableSet() throws Exception {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("jphp");
        engine.put("foo", "bar");
        Object value = engine.eval("<?php return $foo;");
        Assert.assertEquals("bar", value.toString());
    }

    @Test
    public void testVariableGet() throws Exception {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("jphp");
        engine.eval("<?php $baz = 'test value';");
        Assert.assertEquals("test value", engine.get("baz").toString());
    }

    @Test
    public void testCompilable() throws Exception {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("jphp");
        CompiledScript script = ((Compilable)engine).compile("<?php return $foo;");
        engine.put("foo", "first");
        Assert.assertEquals("first", script.eval().toString());
        engine.put("foo", "second");
        Assert.assertEquals("second", script.eval().toString());
    }
}
