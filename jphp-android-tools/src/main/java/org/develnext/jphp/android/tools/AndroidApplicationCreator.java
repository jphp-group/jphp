package org.develnext.jphp.android.tools;

import org.develnext.jphp.core.compiler.jvm.JvmCompiler;
import php.runtime.Information;
import php.runtime.common.StringUtils;
import php.runtime.env.CompileScope;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.ext.support.Extension;
import php.runtime.loader.dump.ModuleDumper;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.ModuleEntity;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class AndroidApplicationCreator {
    private static final String DEX_IN_JAR_NAME = "classes.dex";
    private static final Attributes.Name CREATED_BY = new Attributes.Name("Created-By");

    protected final CompileScope scope;
    protected final Environment env;

    protected List<ModuleEntity> modules;

    public AndroidApplicationCreator() {
        scope = new CompileScope();
        env   = new Environment(scope);
        modules = new ArrayList<ModuleEntity>();
    }

    public AndroidApplicationCreator(CompileScope scope) {
        this.scope = scope;
        this.env   = new Environment(scope);
        this.modules = new ArrayList<ModuleEntity>();
    }

    public CompileScope getScope() {
        return scope;
    }

    public Environment getEnvironment() {
        return env;
    }

    public void registerExtensionJar(File file) {
        try {
            ZipFile zipFile = new ZipFile(file, ZipFile.OPEN_READ);
            ZipEntry entry = zipFile.getEntry("JPHP-INF/extensions.list");

            if (entry != null) {
                Scanner scanner = new Scanner(zipFile.getInputStream(entry));
                while (scanner.hasNext()) {
                    String line = scanner.nextLine().trim();

                    if (!line.isEmpty()) {
                        try {
                            Extension extension = (Extension) Class.forName(line).newInstance();
                            scope.registerExtension(extension);
                        } catch (ClassNotFoundException e) {
                            // nop.
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }


            zipFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

            for (ModuleEntity moduleEntity : modules) {
                outputResources.put(moduleEntity.getInternalName() + ".class", moduleEntity.getData());

                for (ClassEntity classEntity : moduleEntity.getClasses()) {
                    outputResources.put(classEntity.getInternalName() + ".class", classEntity.getData());
                }

                for (FunctionEntity functionEntity : moduleEntity.getFunctions()) {
                    outputResources.put(functionEntity.getInternalName() + ".class", functionEntity.getData());
                }
            }

            //outputResources.put(DEX_IN_JAR_NAME, dexClient.write());

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
            outputResources.put("JPHP-ANDROID-INF/extensions.list", StringUtils.join(scope.getExtensions(), "\n").getBytes());

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
        //dexClient = androidAdapter.adapt(moduleEntity);
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
        attribs.put(CREATED_BY, "jphp " + Information.CORE_VERSION);
        attribs.putValue("Dex-Location", DEX_IN_JAR_NAME);
        return manifest;
    }
}
