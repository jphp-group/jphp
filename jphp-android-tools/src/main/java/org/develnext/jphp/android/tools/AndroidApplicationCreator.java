package org.develnext.jphp.android.tools;


import com.android.dx.Version;
import org.develnext.jphp.android.ext.AndroidExtension;
import org.develnext.jphp.core.compiler.jvm.JvmCompiler;
import php.runtime.env.CompileScope;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.loader.dump.ModuleDumper;
import php.runtime.reflection.ModuleEntity;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class AndroidApplicationCreator {
    private static final String DEX_IN_JAR_NAME = "classes.dex";
    private static final Attributes.Name CREATED_BY = new Attributes.Name("Created-By");

    protected final CompileScope scope;
    protected final Environment env;
    protected final AndroidAdapter androidAdapter = new AndroidAdapter();

    protected DexClient dexClient;
    protected List<ModuleEntity> modules;

    public AndroidApplicationCreator() {
        this(new CompileScope());
    }

    public AndroidApplicationCreator(CompileScope scope) {
        this.scope = scope;

        scope.registerExtension(new AndroidExtension(null));

        this.env   = new Environment(scope);
        this.modules = new ArrayList<ModuleEntity>();
    }

    public CompileScope getScope() {
        return scope;
    }

    public Environment getEnvironment() {
        return env;
    }

    public boolean saveTo(File file) throws IOException {
        File parentFile = file.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            parentFile.mkdirs();
        }

        try {
            TreeMap<String, byte[]> outputResources = new TreeMap<String, byte[]>();
            Manifest manifest = makeManifest();

            OutputStream out = new FileOutputStream(file);
            JarOutputStream jarOut = new JarOutputStream(out, manifest);

            outputResources.put(DEX_IN_JAR_NAME, dexClient.write());

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            DataOutputStream dataOutput  = new DataOutputStream(output);

            dataOutput.writeInt(modules.size());
            for (ModuleEntity module : modules) {
                ModuleDumper dumper = new ModuleDumper(module.getContext(), env, true);

                ByteArrayOutputStream oneOutput = new ByteArrayOutputStream();
                dumper.save(module, oneOutput);

                dataOutput.writeUTF(module.getName());
                dataOutput.writeInt(oneOutput.size());
                dataOutput.write(oneOutput.toByteArray());
            }
            outputResources.put("classes.dump", output.toByteArray());

            try {
                for (Map.Entry<String, byte[]> e : outputResources.entrySet()) {
                    String name = e.getKey();
                    byte[] contents = e.getValue();
                    JarEntry entry = new JarEntry(name);
                    entry.setSize(contents.length);
                    jarOut.putNextEntry(entry);
                    jarOut.write(contents);
                    jarOut.closeEntry();
                }
            } finally {
                jarOut.finish();
                jarOut.flush();
                out.flush();
                out.close();
                jarOut.close();
            }
        } catch (Exception ex) {
            System.out.println("Exception writing jar: " + file.getParent());
            System.out.println("Exception writing jar: " + ex);
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public void addModule(ModuleEntity moduleEntity) {
        dexClient = androidAdapter.adapt(moduleEntity);
        modules.add(moduleEntity);
    }

    public void addFile(File file) throws IOException {
        addFile(file, Charset.defaultCharset());
    }

    public void addFile(File file, Charset charset) throws IOException {
        if (file.isDirectory()) {
            _addDirectory(file, charset);
        } else {
            JvmCompiler jvmCompiler = new JvmCompiler(env, new Context(file, charset));
            ModuleEntity moduleEntity = jvmCompiler.compile(false);

            addModule(moduleEntity);
        }
    }

    protected void _addDirectory(final File file, Charset charset) throws IOException {
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return file.isDirectory() || file.getPath().endsWith(".php");
            }
        });

        for (File el : files) {
            addFile(el, charset);
        }
    }

    protected Manifest makeManifest() throws IOException {
        Manifest manifest = new Manifest();
        Attributes attribs = manifest.getMainAttributes();
        attribs.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        attribs.put(CREATED_BY, "dx " + Version.VERSION);
        attribs.putValue("Dex-Location", DEX_IN_JAR_NAME);
        return manifest;
    }
}
