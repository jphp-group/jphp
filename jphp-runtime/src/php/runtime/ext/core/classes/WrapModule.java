package php.runtime.ext.core.classes;

import java.io.*;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import php.runtime.Memory;
import php.runtime.common.AbstractCompiler;
import php.runtime.common.HintType;
import php.runtime.env.*;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.core.reflection.ReflectionClass;
import php.runtime.ext.core.reflection.ReflectionFunction;
import php.runtime.lang.BaseObject;
import php.runtime.loader.dump.ModuleDumper;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.BinaryMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ConstantEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.ModuleEntity;
import php.runtime.reflection.helper.ClosureEntity;
import php.runtime.reflection.helper.GeneratorEntity;
import php.runtime.reflection.support.Entity;

import static php.runtime.annotation.Reflection.*;

@Name("php\\lang\\Module")
public class WrapModule extends BaseObject {
    protected ModuleEntity module;
    protected boolean registered = false;

    public WrapModule(Environment env, ModuleEntity module) {
        super(env);
        this.module = module;
    }

    public WrapModule(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature({
            @Arg("source"),
            @Arg(value = "compiled", optional = @Optional("false")),
            @Arg(value = "debugInformation", optional = @Optional("true"))
    })
    public Memory __construct(Environment env, Memory... args) throws Throwable {
        InputStream is = Stream.getInputStream(env, args[0]);
        try {
            if (is != null) {
                is = new BufferedInputStream(is);
            }

            Context context = new Context(is, Stream.getPath(args[0]), env.getDefaultCharset());
            if (args[1].toBoolean()) {
                ModuleDumper moduleDumper = new ModuleDumper(context, env, args[2].toBoolean());
                module = moduleDumper.load(context.getInputStream(env.getDefaultCharset()));
            } else {
                AbstractCompiler compiler = env.scope.createCompiler(env, context);
                module = compiler.compile(false);
            }
            register(env);
        } finally {
            Stream.closeStream(env, is);
        }
        return Memory.NULL;
    }

    protected void loadModule(Environment env) {
        if (!module.isLoaded()) {
            synchronized (env.scope) {
                if (!module.isLoaded()) {
                    env.scope.loadModule(module);
                    env.scope.addUserModule(module);
                }
            }
        }
    }

    protected void register(Environment env, Memory... args) {
        if (registered)
            return;

        loadModule(env);
        env.registerModule(module);

        registered = true;
    }

    @Signature
    public String getName() {
        return module.getName();
    }

    @Signature(@Arg(value = "variables", type = HintType.ARRAY, optional = @Optional("NULL")))
    public Memory call(Environment env, Memory... args) throws Throwable {
        if (!registered)
            register(env);

        if (args[0].isNull())
            return module.include(env);
        else
            return module.include(env, args[0].toImmutable().toValue(ArrayMemory.class));
    }

    @Signature
    public void cleanData() {
        module.setData(null);

        for (ClassEntity entity : module.getClasses()) {
            if (!entity.isTrait()) {
                entity.setData(null);
            }
        }

        for (FunctionEntity entity : module.getFunctions()) {
            entity.setData(null);
        }

        for (GeneratorEntity entity : module.getGenerators()) {
            entity.setData(null);
        }

        for (ClosureEntity entity : module.getClosures()) {
            entity.setData(null);
        }
    }

    @Signature
    public Memory getClasses(Environment env) {
        Collection<ClassEntity> classes = module.getClasses();

        ArrayMemory result = new ArrayMemory();
        for (ClassEntity aClass : classes) {
            ReflectionClass rf = new ReflectionClass(env);
            rf.setEntity(aClass);

            result.put(aClass.getName(), rf);
        }

        return result.toConstant();
    }

    @Signature
    public Memory getFunctions(Environment env) {
        Collection<FunctionEntity> functions = module.getFunctions();

        ArrayMemory result = new ArrayMemory();
        for (FunctionEntity func : functions) {
            result.put(func.getName(), new ReflectionFunction(env, func));
        }

        return result.toConstant();
    }

    @Signature
    public Memory getConstants(Environment env) {
        Collection<ConstantEntity> constants = module.getConstants();

        ArrayMemory result = new ArrayMemory();
        for (ConstantEntity constant : constants) {
            result.put(constant.getName(), constant.getValue(env));
        }

        return result.toConstant();
    }

    @Signature
    public Memory getData() {
        return new BinaryMemory(module.getData());
    }

    private File saveJavaClass(File file, byte[] data) throws IOException {
        File parentFile = file.getParentFile();

        if (parentFile != null && !parentFile.isDirectory()) {
            parentFile.mkdirs();
        }

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(data);
        }

        return file;
    }

    @Signature({
            @Arg(value = "targetDir", type = HintType.STRING),
            @Arg(value = "saveDebugInfo", optional = @Optional("true"), type = HintType.BOOLEAN)
    })
    public Memory dumpJVMClasses(Environment env, Memory... args) throws IOException {
        String targetDir = args[0].toString();
        Function<Entity, File> entityClassFile = (Entity entity) -> new File(targetDir, "/" + entity.getInternalName() + ".class");

        ArrayMemory result = new ArrayMemory();

        File moduleClsFile = entityClassFile.apply(module);
        File moduleDumpFile = new File(targetDir, "/" + module.getInternalName() + ".dump");

        result.put("module", saveJavaClass(moduleClsFile, module.getData()).toString());
        result.put("moduleDump", moduleDumpFile.toString());

        ArrayMemory classes = new ArrayMemory();
        for (ClassEntity classEntity : module.getClasses()) {
            if (classEntity.getData() != null) {
                File file = saveJavaClass(entityClassFile.apply(classEntity), classEntity.getData());
                classes.put(classEntity.getName(), file.toString());
            }
        }
        result.put("classes", classes);

        ArrayMemory functions = new ArrayMemory();
        for (FunctionEntity functionEntity : module.getFunctions()) {
            if (functionEntity.getData() != null) {
                File file = saveJavaClass(entityClassFile.apply(functionEntity), functionEntity.getData());
                functions.put(functionEntity.getName(), file.toString());
            }
        }
        result.put("functions", functions);

        ArrayMemory closures = new ArrayMemory();
        for (ClosureEntity one : module.getClosures()) {
            if (one.getData() != null) {
                File file = saveJavaClass(entityClassFile.apply(one), one.getData());
                closures.add(file.toString());
            }
        }
        result.put("closures", closures);

        ArrayMemory generators = new ArrayMemory();
        for (GeneratorEntity one : module.getGenerators()) {
            if (one.getData() != null) {
                File file = saveJavaClass(entityClassFile.apply(one), one.getData());
                generators.add(file.toString());
            }
        }
        result.put("generators", generators);

        ModuleDumper moduleDumper = new ModuleDumper(module.getContext(), env, args[1].toBoolean());
        moduleDumper.setIncludeData(false);

        try (OutputStream os = new FileOutputStream(moduleDumpFile)) {
            moduleDumper.save(module, os);
        }

        result.put("constants", getConstants(env));

        return result.toConstant();
    }

    @Signature({
            @Arg("target"),
            @Arg(value = "saveDebugInfo", optional = @Optional("true"), type = HintType.BOOLEAN),
            @Arg(value = "includeJvmData", optional = @Optional("true"), type = HintType.BOOLEAN)
    })
    public Memory dump(Environment env, Memory... args) throws IOException {
        ModuleDumper moduleDumper = new ModuleDumper(module.getContext(), env, args[1].toBoolean());
        moduleDumper.setIncludeData(args[2].toBoolean());

        OutputStream os = Stream.getOutputStream(env, args[0]);
        try {
            moduleDumper.save(module, os);
        } finally {
            Stream.closeStream(env, os);
        }
        return Memory.NULL;
    }
}
