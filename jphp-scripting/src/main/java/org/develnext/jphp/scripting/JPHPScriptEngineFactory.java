package org.develnext.jphp.scripting;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class JPHPScriptEngineFactory implements ScriptEngineFactory {

    private static List<String> names;
    private static List<String> extensions;
    private static List<String> mimeTypes;
    private static final ThreadLocal<JPHPScriptEngine> engines
            = new ThreadLocal<JPHPScriptEngine>();

    static {
        names = new ArrayList<String>(2);
        names.add("jphp");
        names.add("php");
        names = Collections.unmodifiableList(names);
        extensions = new ArrayList<String>(1);
        extensions.add("php");
        extensions = Collections.unmodifiableList(extensions);
        mimeTypes = new ArrayList<String>(4);
        mimeTypes.add("text/php");
        mimeTypes.add("text/x-php");
        mimeTypes.add("application/php");
        mimeTypes.add("application/x-php");
        mimeTypes = Collections.unmodifiableList(mimeTypes);
    }

    @Override
    public String getEngineName() {
        return getScriptEngine().get(ScriptEngine.ENGINE).toString();
    }

    @Override
    public String getEngineVersion() {
        return getScriptEngine().get(ScriptEngine.ENGINE_VERSION).toString();
    }

    @Override
    public List<String> getExtensions() {
        return extensions;
    }

    @Override
    public List<String> getMimeTypes() {
        return mimeTypes;
    }

    @Override
    public List<String> getNames() {
        return names;
    }

    @Override
    public String getLanguageName() {
        return getScriptEngine().get(ScriptEngine.LANGUAGE).toString();
    }

    @Override
    public String getLanguageVersion() {
        return getScriptEngine().get(ScriptEngine.LANGUAGE_VERSION).toString();
    }

    @Override
    public Object getParameter(String key) {
        return getScriptEngine().get(key).toString();
    }

    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) {
        StringBuilder buf = new StringBuilder();
        buf.append('$');
        buf.append(obj);
        buf.append("->");
        buf.append(m);
        buf.append('(');
        if (args.length != 0) {
            int i = 0;
            for (; i < args.length - 1; i++) {
                buf.append('$');
                buf.append(args[i] + ", ");
            }
            buf.append('$');
            buf.append(args[i]);
        }
        buf.append(')');
        return buf.toString();
    }

    @Override
    public String getOutputStatement(String toDisplay) {
        StringBuilder buf = new StringBuilder();
        buf.append("print('");
        int len = toDisplay.length();
        for (int i = 0; i < len; i++) {
            char ch = toDisplay.charAt(i);
            switch (ch) {
                case '\'':
                    buf.append("\\\'");
                    break;
                case '\\':
                    buf.append("\\\\");
                    break;
                default:
                    buf.append(ch);
                    break;
            }
        }
        buf.append("')");
        return buf.toString();
    }

    @Override
    public String getProgram(String... statements) {
        return null;
    }

    @Override
    public ScriptEngine getScriptEngine() {
        JPHPScriptEngine engine = engines.get();
        if (engine == null) {
            engine = new JPHPScriptEngine();
            engine.setFactory(this);
            engines.set(engine);
        }
        return engine;
    }
}
