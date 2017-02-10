package php.runtime.env;

import php.runtime.Memory;
import php.runtime.common.Messages;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ReferenceMemory;

import java.util.*;

public class PackageManager {
    private final Map<String, Package> packages = new HashMap<>();
    private final Set<PackageLoader> loaders = new LinkedHashSet<>();
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
        return packages.containsKey(name);
    }

    public Package tryFind(String name, TraceInfo trace) {
        Package aPackage = packages.get(name);

        if (aPackage == null) {
            synchronized (loaders) {
                for (PackageLoader loader : loaders) {
                    Package newPackage = loader.load(name, trace);

                    if (newPackage != null) {
                        synchronized (packages) {
                            packages.put(name, newPackage);
                            return newPackage;
                        }
                    }
                }

                return null;
            }
        }

        return aPackage;
    }

    public Package fetch(String name) {
        Package aPackage = packages.get(name);

        if (aPackage == null) {
            synchronized (packages) {
                aPackage = packages.get(name);

                if (aPackage != null) return aPackage;

                packages.put(name, aPackage = new Package());
            }
        }

        return aPackage;
    }

    synchronized public boolean registerLoader(PackageLoader loader) {
        return loaders.add(loader);
    }

    synchronized public boolean unregisterLoader(PackageLoader loader) {
        return loaders.remove(loader);
    }

    synchronized public void set(String name, Package aPackage) {
        packages.put(name, aPackage);
    }
}
