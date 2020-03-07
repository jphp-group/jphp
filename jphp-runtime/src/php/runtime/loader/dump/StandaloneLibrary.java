package php.runtime.loader.dump;

import php.runtime.common.collections.map.HashedMap;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ConstantEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.ModuleEntity;

import java.util.*;

public class StandaloneLibrary {
    private String coreVersion;
    private String likePhpVersion;
    private Map<String, Module> modules = new LinkedHashMap<>();

    private Map<String, Module> classModules = new HashMap<>();
    private Map<String, Module> functionModules = new HashMap<>();
    private Map<String, Module> constantModules = new HashMap<>();

    public Map<String, Module> getModules() {
        return modules;
    }

    public Map<String, Module> getClassModules() {
        return classModules;
    }

    public Map<String, Module> getFunctionModules() {
        return functionModules;
    }

    public Map<String, Module> getConstantModules() {
        return constantModules;
    }

    public void addModule(Module module) {
        modules.put(module.getName(), module);

        for (String name : module.getClasses()) {
            classModules.put(name.toLowerCase(), module);
        }

        for (String name : module.getFunctions()) {
            functionModules.put(name.toLowerCase(), module);
        }

        for (String name : module.getConstants()) {
            constantModules.put(name, module);
        }
    }

    public String getCoreVersion() {
        return coreVersion;
    }

    public void setCoreVersion(String coreVersion) {
        this.coreVersion = coreVersion;
    }

    public String getLikePhpVersion() {
        return likePhpVersion;
    }

    public void setLikePhpVersion(String likePhpVersion) {
        this.likePhpVersion = likePhpVersion;
    }

    public static class Module {
        private final String name;
        private final String internalName;

        private final Set<String> classes;
        private final Set<String> functions;
        private final Set<String> constants;

        public Module(ModuleEntity entity) {
            name = entity.getName();
            internalName = entity.getInternalName();

            classes = new LinkedHashSet<>();
            for (ClassEntity classEntity : entity.getClasses()) {
                classes.add(classEntity.getName());
            }

            functions = new LinkedHashSet<>();
            for (FunctionEntity funcEntity : entity.getFunctions()) {
                functions.add(funcEntity.getName());
            }

            constants = new LinkedHashSet<>();
            for (ConstantEntity constEntity : entity.getConstants()) {
                constants.add(constEntity.getName());
            }
        }

        public Module(String name, String internalName,
                      Set<String> classes, Set<String> functions, Set<String> constants) {
            this.name = name;
            this.internalName = internalName;
            this.classes = classes;
            this.functions = functions;
            this.constants = constants;
        }

        public String getName() {
            return name;
        }

        public String getInternalName() {
            return internalName;
        }

        public Set<String> getClasses() {
            return classes;
        }

        public Set<String> getFunctions() {
            return functions;
        }

        public Set<String> getConstants() {
            return constants;
        }
    }
}
