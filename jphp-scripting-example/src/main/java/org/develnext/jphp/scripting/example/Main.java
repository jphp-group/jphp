package org.develnext.jphp.scripting.example;


import javax.script.*;

public class Main {

    public static void main(String[] args) throws Exception {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("php");
        Compilable compilableEngine = (Compilable)engine;
        CompiledScript script = compilableEngine.compile("<?php print('Hello, ' . $test);");
        script.getEngine().put("test", "world\n");
        script.eval();
        script.getEngine().put("test", "another world\n");
        script.eval();
        engine.eval("<?php $foo = 'bar';");
        System.out.println(engine.get("foo").toString());
    }
}
