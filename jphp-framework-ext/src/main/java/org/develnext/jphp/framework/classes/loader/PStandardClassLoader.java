package org.develnext.jphp.framework.classes.loader;

import org.develnext.jphp.framework.FrameworkExtension;
import php.runtime.Information;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.WrapModule;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.core.classes.stream.WrapIOException;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ModuleEntity;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Name(FrameworkExtension.NS + "loader\\StandardClassLoader")
public class PStandardClassLoader extends PClassLoader {
    protected Map<String, Set<String>> prefixes = new LinkedHashMap<String, Set<String>>();
    protected final Map<String, ModuleEntity> modules = new LinkedHashMap<String, ModuleEntity>();

    public PStandardClassLoader(Environment env) {
        super(env);
    }

    public PStandardClassLoader(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    synchronized public void addPrefixes(Environment env, ForeachIterator iterator) {
        while (iterator.next()) {
            addPrefix(env, iterator.getKey().toString(), iterator.getValue());
        }
    }

    @Signature
    synchronized public void addPrefix(Environment env, String prefix, Memory directories) {
        Set<String> list = prefixes.get(prefix);

        if (list == null) {
            prefixes.put(prefix, list = new TreeSet<String>());
        }

        if (directories.isTraversable()) {
            ForeachIterator iterator = directories.getNewIterator(env);

            while (iterator.next()) {
                list.add(iterator.getValue().toString());
            }
        } else {
            list.add(directories.toString());
        }
    }

    @Override
    @Signature
    public boolean loadClass(Environment env, String className) throws Throwable {
        if (className.startsWith(Information.NAMESPACE_SEP)) {
            className = className.substring(1);
        }

        for (Map.Entry<String, Set<String>> el : prefixes.entrySet()) {
            String prefix = el.getKey();

            if (className.startsWith(prefix)) {
                String classWithoutPrefix = className.substring(prefix.length());

                for (String path : el.getValue()) {
                    String file = path + "/" + classWithoutPrefix;

                    file = file.replace('\\', '/');

                    if (file.startsWith("\\") || file.startsWith("/")) {
                        file = file.substring(1);
                    }

                    ModuleEntity entity = fetchClass(env, file + ".php");

                    if (entity != null) {
                        synchronized (modules) {
                            modules.put(file, entity);
                        }

                        entity.include(env);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    protected ModuleEntity fetchClass(Environment env, String fileName) throws Throwable {
        try {
            Memory value = Stream.of(env, StringMemory.valueOf(fileName), StringMemory.valueOf("r"));
            return env.importModule(new Context(Stream.getInputStream(env, value), fileName, env.getDefaultCharset()));
        } catch (WrapIOException throwable) {
            return null;
        }
    }
}
