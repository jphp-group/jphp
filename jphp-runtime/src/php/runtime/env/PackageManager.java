package php.runtime.env;

import php.runtime.Memory;
import php.runtime.common.Messages;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ReferenceMemory;

import java.util.*;

public class PackageManager {
    private final Map<String, Package> packages = new HashMap<>();
    private final Map<String, Set<String>> autoload = new HashMap<>();
    private final Environment env;

    public PackageManager(Environment env) {
        this.env = env;
    }

    public void apply(PackageManager parent) {
        for (Map.Entry<String, Package> packageEntry : parent.packages.entrySet()) {
            this.packages.put(packageEntry.getKey(), packageEntry.getValue().duplicate());
        }
    }

    public Collection<String> names() {
        return packages.keySet();
    }

    public boolean has(String name) {
        return packages.containsKey(name) || autoload.containsKey(name);
    }

    public Package fetch(String name, TraceInfo trace) {
        Package aPackage = packages.get(name);

        if (aPackage == null) {
            synchronized (packages) {
                aPackage = packages.get(name);

                if (aPackage != null) return aPackage;

                packages.put(name, aPackage = new Package(name));
            }
        }

        Set<String> strings = autoload.get(name);

        if (strings != null && !strings.isEmpty()) {
            List<String> tmp = new ArrayList<>();

            for (String string : strings) {
                if (env.getModuleManager().hasModule(string)) {
                    continue;
                }

                tmp.add(string);
            }

            if (!tmp.isEmpty()) {
                synchronized (autoload) {
                    for (String string : tmp) {
                        try {
                            Memory memory = env.__require(string, null, trace);

                            if (!memory.isArray()) {
                                env.error(trace, ErrorType.E_ERROR, Messages.ERR_PACKAGE_FILE_MUST_RETURN_ARRAY, string, name);
                            } else {
                                ArrayMemory arrayMemory = memory.toValue(ArrayMemory.class);

                                ReferenceMemory classes = arrayMemory.getByScalar("classes");
                                ReferenceMemory functions = arrayMemory.getByScalar("functions");
                                ReferenceMemory constants = arrayMemory.getByScalar("constants");

                                if (classes != null && !classes.isArray()) {
                                    env.error(trace, ErrorType.E_ERROR, Messages.ERR_PACKAGE_FILE_MUST_RETURN_ARRAY, string, name);
                                }

                                if (functions != null && !functions.isArray()) {
                                    env.error(trace, ErrorType.E_ERROR, Messages.ERR_PACKAGE_FILE_MUST_RETURN_ARRAY, string, name);
                                }

                                if (constants != null && !constants.isArray()) {
                                    env.error(trace, ErrorType.E_ERROR, Messages.ERR_PACKAGE_FILE_MUST_RETURN_ARRAY, string, name);
                                }

                                if (classes != null) {
                                    for (Memory one : classes.toValue(ArrayMemory.class)) {
                                        aPackage.addClass(one.toString());
                                    }
                                }

                                if (functions != null) {
                                    for (Memory one : functions.toValue(ArrayMemory.class)) {
                                        aPackage.addFunction(one.toString());
                                    }
                                }

                                if (constants != null) {
                                    for (Memory one : constants.toValue(ArrayMemory.class)) {
                                        aPackage.addConstant(one.toString());
                                    }
                                }
                            }
                        } catch (Throwable throwable) {
                            env.forwardThrow(throwable);
                        }
                    }

                    strings.clear();
                }
            }
        }

        return aPackage;
    }

    synchronized public void addAutoload(String pkg, String path) {
        Set<String> strings = autoload.get(pkg);

        if (strings == null) {
            autoload.put(pkg, strings = new HashSet<>());
        }

        strings.add(path);
    }

}
