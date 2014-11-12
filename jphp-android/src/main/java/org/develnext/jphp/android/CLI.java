package org.develnext.jphp.android;

import php.runtime.launcher.Launcher;
import php.runtime.loader.dump.ModuleDumper;
import php.runtime.reflection.ModuleEntity;

import java.io.FileOutputStream;
import java.io.FileWriter;

public class CLI {
    protected static final AndroidAdapter androidAdapter = new AndroidAdapter();

    public static void main(String[] args) throws Throwable {
        Launcher launcher = new Launcher("jphp.conf", args);
        launcher.run(false);

        ModuleEntity entity = launcher.loadFromFile(args[0]);
        DexClient dexClient = androidAdapter.adapt(entity);

        ModuleDumper moduleDumper = new ModuleDumper(entity.getContext(), launcher.getEnvironment(), true);
        moduleDumper.save(entity, new FileOutputStream(args[0] + ".dump"));
        dexClient.write(new FileWriter(args[0] + ".dex"));
    }
}
