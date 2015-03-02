package org.develnext.jphp.framework.classes.loader;

import org.develnext.jphp.framework.FrameworkExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.Constants;
import php.runtime.env.Environment;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.io.File;
import java.util.*;

@Name(FrameworkExtension.NS + "loader\\FileClassLoader")
public class PFileClassLoader extends PClassLoader {
    protected Map<String, Set<File>> prefixes = new LinkedHashMap<String, Set<File>>();

    public PFileClassLoader(Environment env) {
        super(env);
    }

    public PFileClassLoader(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    synchronized public void addPrefix(Environment env, String prefix, Memory directories) {
        if (prefix.isEmpty()) {
            prefix = null;
        }

        Set<File> list = prefixes.get(prefix);

        if (list == null) {
            prefixes.put(prefix, list = new TreeSet<File>());
        }

        if (directories.isTraversable()) {
            ForeachIterator iterator = directories.getNewIterator(env);

            while (iterator.next()) {
                list.add(new File(iterator.getValue().toString()));
            }
        } else {
            list.add(new File(directories.toString()));
        }
    }

    @Signature
    synchronized public void addPrefixes(Environment env, ForeachIterator iterator) {
        while (iterator.next()) {
            addPrefix(env, iterator.getKey().toString(), iterator.getValue());
        }
    }

    @Override
    @Signature
    public boolean loadClass(Environment env, String className) throws Throwable {
        String path = findClass(className);

        if (path != null) {
            env.__require(path, new ArrayMemory(), env.trace());
            return true;
        }

        return false;
    }

    @Signature
    public String findClass(String clazz) {
        int pos = clazz.lastIndexOf('\\');

        String classPath, className;

        if (pos > -1) {
            classPath = clazz.substring(0, pos).replace("\\", Constants.DIRECTORY_SEPARATOR) + Constants.DIRECTORY_SEPARATOR;
            className = clazz.substring(pos + 1);
        } else {
            classPath = "";
            className = clazz;
        }

        classPath += className.replace("_", Constants.DIRECTORY_SEPARATOR) + ".php";

        for (Map.Entry<String, Set<File>> el : prefixes.entrySet()) {
            String prefix = el.getKey();

            if (prefix != null && clazz.startsWith(prefix)) {
                for (File directory : el.getValue()) {
                    File file = new File(directory, Constants.DIRECTORY_SEPARATOR + classPath);

                    if (file.exists()) {
                        return file.getPath();
                    }
                }
            }
        }

        if (prefixes.containsKey(null)) {
            for (File directory : prefixes.get(null)) {
                File file = new File(directory, Constants.DIRECTORY_SEPARATOR + classPath);

                if (file.exists()) {
                    return file.getPath();
                }
            }
        }

        return null;
    }
}
