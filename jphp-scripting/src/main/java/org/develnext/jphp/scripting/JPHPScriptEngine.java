package org.develnext.jphp.scripting;

import org.develnext.jphp.scripting.util.ReaderInputStream;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.launcher.Launcher;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ModuleEntity;

import javax.script.*;
import java.io.*;

public class JPHPScriptEngine extends AbstractScriptEngine implements Compilable {

    private static final String __ENGINE_VERSION__   = "0.4";
    private static final String __NAME__             = "JPHP Engine";
    private static final String __SHORT_NAME__       = "jphp";
    private static final String __LANGUAGE__         = "php";
    private static final String __LANGUAGE_VERSION__ = "5.3";

    private ScriptEngineFactory factory = null;

    public JPHPScriptEngine() {
        super();
        JPHPContext ctx = new JPHPContext();
        ctx.setBindings(createBindings(), ScriptContext.ENGINE_SCOPE);
        setContext(ctx);

        put(LANGUAGE_VERSION, __LANGUAGE_VERSION__);
        put(LANGUAGE, __LANGUAGE__);
        put(ENGINE, __NAME__);
        put(ENGINE_VERSION, __ENGINE_VERSION__);
        put(NAME, __SHORT_NAME__);
    }



    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        return eval(new StringReader(script), context);
    }

    @Override
    public Object eval(Reader reader, ScriptContext _ctx) throws ScriptException {
        return compile(reader).eval(_ctx);
    }

    @Override
    public CompiledScript compile(String script) throws ScriptException {
        return compile(new StringReader(script));
    }

    @Override
    public CompiledScript compile(Reader reader) throws ScriptException {
        try {
            InputStream is = new ReaderInputStream(reader);
            Launcher launcher = new Launcher();
            launcher.run(false);

            Environment environment = new Environment(launcher.getCompileScope(), System.out);
            environment.getDefaultBuffer().setImplicitFlush(true);
            Context context = new Context(is);
            ModuleEntity module = environment.importModule(context);
            return new JPHPCompiledScript(module, environment);
        } catch (IOException e) {
            throw new ScriptException(e);
        } catch (Throwable e) {
            throw new ScriptException(new Exception(e));
        }
    }

    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    @Override
    public synchronized ScriptEngineFactory getFactory() {
        if (factory == null) {
            factory = new JPHPScriptEngineFactory();
        }
        return factory;
    }

    public void setFactory(JPHPScriptEngineFactory f) {
        factory = f;
    }

    public class JPHPCompiledScript extends CompiledScript {

        private ModuleEntity module;
        private Environment environment;
        public JPHPCompiledScript(ModuleEntity m, Environment env) {
            module = m;
            environment = env;
        }

        @Override
        public Object eval(ScriptContext context) throws ScriptException {
            try {
                try {
                    Bindings b = context.getBindings(ScriptContext.ENGINE_SCOPE);
                    ArrayMemory arr = new ArrayMemory(b);
                    environment.getGlobals().putAll(arr);
                    return module.include(environment);
                } catch (Exception e) {
                    environment.catchUncaught(e);
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                } finally {
                    try {
                        environment.doFinal();
                    } catch (Throwable throwable) {
                        throw new RuntimeException(throwable);
                    }
                }
            } catch (Throwable e) {
                throw new ScriptException(new Exception(e));
            }
            return null;
        }

        @Override
        public ScriptEngine getEngine() {
            return JPHPScriptEngine.this;
        }
    }
}
