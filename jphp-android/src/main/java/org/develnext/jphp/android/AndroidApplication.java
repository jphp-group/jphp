package org.develnext.jphp.android;

import android.app.Activity;
import android.content.res.AssetManager;
import php.runtime.env.CompileScope;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.ext.support.Extension;
import php.runtime.loader.dump.ModuleDumper;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.ModuleEntity;
import php.runtime.reflection.helper.ClosureEntity;
import php.runtime.reflection.helper.GeneratorEntity;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AndroidApplication {

    public static AndroidApplication INSTANCE;

    protected final CompileScope scope;
    protected final Environment env;
    protected final ClassLoader classLoader;
    protected Activity mainActivity;
    protected AssetManager assetManager;

    protected ClassLoader dalvikClassLoader;

    protected AndroidConfiguration configuration;

    protected ModuleEntity bootstrapModule;
    protected final List<ModuleEntity> modules = new ArrayList<ModuleEntity>();

    public AndroidApplication(CompileScope scope) {
        this.scope        = scope;
        this.env          = new Environment(scope);
        this.classLoader  = scope.getClassLoader();

        INSTANCE = this;
    }

    public void setMainActivity(Activity mainActivity) {
        this.mainActivity = mainActivity;
        this.assetManager = mainActivity.getAssets();

        dalvikClassLoader = mainActivity.getClassLoader();
        configuration     = new AndroidConfiguration();
    }

    public Environment getEnvironment() {
        return env;
    }

    public Activity getMainActivity() {
        return mainActivity;
    }

    public android.content.Context getContext() {
        return mainActivity.getApplicationContext();
    }

    public List<ModuleEntity> loadLibrary() throws IOException {
        Scanner scanner = new Scanner(dalvikClassLoader.getResourceAsStream("JPHP-ANDROID-INF/extensions.list"));

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (!line.isEmpty()) {
                try {
                    scope.registerExtension((Extension) Class.forName(line).newInstance());
                } catch (ClassNotFoundException e) {
                    // nop.
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        List<ModuleEntity> result = new ArrayList<ModuleEntity>();

        DataInputStream classesDump = new DataInputStream(dalvikClassLoader.getResourceAsStream("classes.dump"));

        int classSize = classesDump.readInt();

        for (int i = 0; i < classSize; i++) {
            String moduleName = classesDump.readUTF();
            int moduleLength = classesDump.readInt();
            byte[] data = new byte[moduleLength];

            classesDump.read(data);

            ModuleDumper moduleDumper = new ModuleDumper(new Context(new File(moduleName)), env, true);

            ModuleEntity moduleEntity = moduleDumper.load(new ByteArrayInputStream(data));
            loadModule(moduleEntity, dalvikClassLoader);

            scope.loadModule(moduleEntity, false);
            env.registerModule(moduleEntity);

            result.add(moduleEntity);

            if (moduleEntity.getContext().getModuleName().endsWith("bootstrap.php")) {
                bootstrapModule = moduleEntity;
            }
        }

        modules.addAll(result);
        return result;
    }

    public void start() {
        if (bootstrapModule != null) {
            bootstrapModule.includeNoThrow(env);
        } else {
            modules.get(0).includeNoThrow(env);
        }
    }

    protected void loadModule(ModuleEntity moduleEntity, ClassLoader dexClassLoader) {
        try {
            moduleEntity.setNativeClazz(dexClassLoader.loadClass(moduleEntity.getInternalName()));

            for (FunctionEntity functionEntity : moduleEntity.getFunctions()) {
                functionEntity.setNativeClazz(
                        dexClassLoader.loadClass(functionEntity.getInternalName())
                );
            }

            for (ClassEntity classEntity : moduleEntity.getClasses()) {
                classEntity.setNativeClazz(
                        dexClassLoader.loadClass(classEntity.getInternalName())
                );
            }

            for (ClosureEntity closureEntity : moduleEntity.getClosures()) {
                closureEntity.setNativeClazz(
                        dexClassLoader.loadClass(closureEntity.getInternalName())
                );
            }

            for (GeneratorEntity generatorEntity : moduleEntity.getGenerators()) {
                generatorEntity.setNativeClazz(
                        dexClassLoader.loadClass(generatorEntity.getInternalName())
                );
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
