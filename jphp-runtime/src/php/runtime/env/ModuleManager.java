package php.runtime.env;

import php.runtime.ext.core.classes.stream.FileStream;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.core.classes.stream.WrapIOException;
import php.runtime.reflection.ModuleEntity;

import java.io.BufferedInputStream;
import java.io.File;
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

    public ModuleEntity fetchModule(String path) throws Throwable {
        return fetchModule(path, path.endsWith(".phb"));
    }

    public ModuleEntity fetchModule(String path, boolean compiled) throws Throwable {
        ModuleEntity moduleEntity = modules.get(path);

        if (moduleEntity != null &&
                (moduleEntity.getContext().getLastModified() == 0
                        || moduleEntity.getContext().getLastModified() == new File(path).lastModified())) {

            return moduleEntity;
        } else {
            env.scope.removeUserModule(path);

            moduleEntity = fetchTemporaryModule(path, compiled);

            if (moduleEntity == null) {
                return null;
            }

            modules.put(path, moduleEntity);

            return moduleEntity;
        }
    }

    public ModuleEntity fetchTemporaryModule(String path, boolean compiled) throws Throwable {
        Stream stream = fetchStream(path);

        if (stream == null) {
            return null;
        }

        try {
            if (stream._isExternalResourceStream()) {
                env.exception("Cannot import module form external stream: " + stream.getPath());
                return null;
            } else {
                ModuleEntity module;
                Context context = fetchContext(stream);

                if (compiled) {
                    module = env.importCompiledModule(context, true);
                } else {
                    module = env.importModule(context);
                }

                module.setTrace(new TraceInfo(context));
                return module;
            }
        } finally {
            env.invokeMethod(stream, "close");
        }
    }

    public Context fetchContext(Stream stream) throws Throwable {
        if (stream instanceof FileStream) {
            return new Context(
                    new BufferedInputStream(Stream.getInputStream(env, stream)), stream.getPath(), env.getDefaultCharset()
            );
        }

        return new Context(
                new BufferedInputStream(Stream.getInputStream(env, stream)), stream.getPath(), env.getDefaultCharset()
        );
    }

    protected Stream fetchStream(String path) throws Throwable {
        try {
            return Stream.create(env, path, "r");
        } catch (WrapIOException e) {
            return null;
        }
    }

    public ModuleEntity findModule(String path) {
        return modules.get(path);
    }

    public void addModule(String path, ModuleEntity module) {
        modules.put(path, module);
    }

    public ModuleEntity findModule(TraceInfo traceInfo) {
        if (traceInfo == null || traceInfo == TraceInfo.UNKNOWN) {
            return null;
        }

        return findModule(traceInfo.getFileName());
    }

    public boolean hasModule(String path) {
        return modules.containsKey(path);
    }

    public Collection<String> getCachedPaths() {
        return modules.keySet();
    }
}
