package php.runtime.env;

import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.core.classes.stream.WrapIOException;
import php.runtime.reflection.ModuleEntity;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModuleManager {
    protected final Environment env;
    protected final Map<String, ModuleEntity> modules = new LinkedHashMap<String, ModuleEntity>();

    public ModuleManager(Environment env) {
        this.env = env;
    }

    public void apply(ModuleManager parent) {
        this.modules.putAll(parent.modules);
    }

    protected ModuleEntity fetchCachedModule(String path) throws Throwable {
        ModuleEntity moduleEntity = modules.get(path);

        if (moduleEntity != null) {
            return moduleEntity;
        } else {
            moduleEntity = fetchModule(path);
            modules.put(path, moduleEntity);

            return moduleEntity;
        }
    }

    protected ModuleEntity fetchModule(String path) throws Throwable {
        Stream stream = fetchStream(path);

        if (stream == null) {
            return null;
        }

        try {
            return env.importModule(fetchContext(stream));
        } finally {
            env.invokeMethod(stream, "close");
        }
    }

    protected Context fetchContext(Stream stream) throws Throwable {
        return new Context(Stream.getInputStream(env, stream));
    }

    protected Stream fetchStream(String path) throws Throwable {
        try {
            return Stream.create(env, path, "r");
        } catch (WrapIOException e) {
            return null;
        }
    }

    public boolean hasModule(String path) {
        return modules.containsKey(path);
    }

    public Collection<String> getCachedPaths() {
        return modules.keySet();
    }
}
