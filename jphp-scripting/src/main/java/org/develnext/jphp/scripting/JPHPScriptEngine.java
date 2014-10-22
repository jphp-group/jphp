package org.develnext.jphp.scripting;

import org.develnext.jphp.scripting.util.ReaderInputStream;
import php.runtime.Information;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.launcher.Launcher;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryUtils;
import php.runtime.reflection.ModuleEntity;

import javax.script.*;
import java.io.*;
import java.util.*;

public class JPHPScriptEngine extends AbstractScriptEngine implements Compilable {

    private static final String __ENGINE_VERSION__   = Information.CORE_VERSION;
    private static final String __NAME__             = Information.NAME;
    private static final String __SHORT_NAME__       = "jphp";
    private static final String __LANGUAGE__         = "php";
    private static final String __LANGUAGE_VERSION__ = Information.LIKE_PHP_VERSION;

    private ScriptEngineFactory factory = null;
    private Environment environment;

    public JPHPScriptEngine() {
        super();

        Launcher launcher = new Launcher();
        try {
            launcher.run(false);
        } catch (Throwable e) {
            //pass
        }
        environment = new Environment(launcher.getCompileScope(), System.out);
        environment.getDefaultBuffer().setImplicitFlush(true);

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
        return new JPHPBindings(environment.getGlobals());
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

    public class JPHPBindings implements Bindings {

        private ArrayMemory globals;

        public JPHPBindings(ArrayMemory globals) {
            this.globals = globals;
        }

        @Override
        public Object put(String name, Object value) {
            return globals.putAsKeyString(name, MemoryUtils.valueOf(value));
        }

        @Override
        public void putAll(Map<? extends String, ? extends Object> toMerge) {
            for (String key : toMerge.keySet()) {
                put(key, toMerge.get(key));
            }
        }

        @Override
        public void clear() {
            globals.clear();
        }

        @Override
        public Set<String> keySet() {
            Set<String> set = new HashSet<String>(size());
            for (Object k : globals.keySet()) {
                set.add(k.toString());
            }
            return set;
        }

        @Override
        public Collection<Object> values() {
            return new ArrayList<Object>(Arrays.asList(globals.values()));
        }

        @Override
        public Set<Entry<String, Object>> entrySet() {
            Set<Entry<String, Object>> set = new HashSet<Entry<String, Object>>(size());
            for (Object k : globals.keySet()) {
                set.add(new AbstractMap.SimpleEntry<String, Object>(k.toString(), get(k)));
            }
            return set;
        }

        @Override
        public int size() {
            return globals.size();
        }

        @Override
        public boolean isEmpty() {
            return size() == 0;
        }

        @Override
        public boolean containsKey(Object key) {
            return globals.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return false;
        }

        @Override
        public Object get(Object key) {
            return globals.getByScalar(key);
        }

        @Override
        public Object remove(Object key) {
            return globals.removeByScalar(key);
        }
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
