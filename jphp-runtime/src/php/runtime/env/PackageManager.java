package php.runtime.env;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PackageManager {
    private final Map<String, Package> packages = new HashMap<>();

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

    public Package fetch(String name) {
        Package aPackage = packages.get(name);

        if (aPackage != null) {
            return aPackage;
        }

        synchronized (packages) {
            aPackage = packages.get(name);

            if (aPackage != null) return aPackage;

            packages.put(name, aPackage = new Package(name));

            return aPackage;
        }
    }
}
