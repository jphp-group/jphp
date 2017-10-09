package php.runtime.loader.compile;

import php.runtime.Information;
import php.runtime.common.Callback;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.lib.FsUtils;
import php.runtime.ext.support.Extension;
import php.runtime.launcher.StandaloneLauncher;
import php.runtime.loader.dump.*;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.FunctionEntity;
import php.runtime.reflection.ModuleEntity;
import php.runtime.reflection.helper.ClosureEntity;
import php.runtime.reflection.helper.GeneratorEntity;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ServiceLoader;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class StandaloneCompiler {
    private final Environment env;
    private File sourceDirectory;

    private boolean debug = true;

    public static void main(String[] args) {
        Environment env = new Environment();

        if (args == null || args.length < 1) {
            System.err.println("ERROR: Pass the first argument (source directory).");
            System.exit(1);
        }

        if (args.length < 2) {
            System.err.println("ERROR: Pass the second argument (destination directory).");
            System.exit(1);
        }

        File sourceDirectory = new File(args[0]);
        File destinationDirectory = new File(args[1]);
        File jarFile = null;

        if (destinationDirectory.getName().endsWith(".jar")) {
            jarFile = destinationDirectory;
            destinationDirectory = new File(destinationDirectory.getPath() + ".temp");
        }

        if (!destinationDirectory.isDirectory()) {
            if (!destinationDirectory.mkdirs()) {
                System.err.println("ERROR: Failed to create destination directory: " + destinationDirectory);
                System.exit(2);
            }
        }

        String[] list = destinationDirectory.list();

        if (list == null || list.length > 0) {
            if (jarFile != null) {
                FsUtils.clean(env, destinationDirectory.getPath());
            } else {
                System.err.println("ERROR: Destination directory must be empty.");
                System.exit(2);
            }
        }

        long time = System.currentTimeMillis();

        System.out.println(
                "Start compiling...\n -> source: " + sourceDirectory + "\n -> destination: " + destinationDirectory + "\n"
        );

        try {
            StandaloneCompiler compiler = new StandaloneCompiler(sourceDirectory, env);

            compiler.compile(destinationDirectory);

            if (jarFile != null) {
                try {
                    compiler.compileJar(jarFile, destinationDirectory, StandaloneLauncher.class.getName());
                } finally {
                    FsUtils.clean(env, destinationDirectory.getPath());
                    FsUtils.delete(destinationDirectory.getPath());
                }
            }

            System.out.println("\nCompiling is SUCCESSFUL, time = " + (System.currentTimeMillis() - time) + "ms.");
        } catch (Throwable e) {
            System.err.println("\nCompiling is FAILED, error: " + e.getMessage());
            System.exit(3);
        }
    }

    public StandaloneCompiler(File sourceDirectory, Environment env) {
        this.env = env;

        if (sourceDirectory == null) {
            throw new NullPointerException("sourceDirectory is null");
        }

        this.sourceDirectory = sourceDirectory;

        loadExtensions();
    }

    private void loadExtensions() {
        ServiceLoader<Extension> loader = ServiceLoader.load(Extension.class);

        for (Extension extension : loader) {
            env.scope.registerExtension(extension);
        }
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    private void saveJavaClass(File file, byte[] data) throws IOException {
        File parentFile = file.getParentFile();

        if (parentFile != null && !parentFile.isDirectory()) {
            parentFile.mkdirs();
        }

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(data);
        }
    }

    private void saveModuleClasses(ModuleEntity entity, File destinationDirectory) throws IOException {
        saveJavaClass(new File(destinationDirectory, "/" + entity.getInternalName() + ".class"), entity.getData());

        Context _context = entity.getContext();
        String moduleName = sourceDirectory
                .toPath()
                .relativize(Paths.get(_context.getModuleName()))
                .toString()
                .replace('\\', '/');

        Context context = new Context(new ByteArrayInputStream(new byte[0]), moduleName, env.getDefaultCharset());

        entity.setContext(context);
        entity.setName(moduleName);

        for (ClassEntity classEntity : entity.getClasses()) {
            saveJavaClass(
                    new File(destinationDirectory, "/" + classEntity.getInternalName() + ".class"),
                    classEntity.getData()
            );
        }

        for (FunctionEntity functionEntity : entity.getFunctions()) {
            saveJavaClass(
                    new File(destinationDirectory, "/" + functionEntity.getInternalName() + ".class"),
                    functionEntity.getData()
            );
        }

        for (ClosureEntity one : entity.getClosures()) {
            saveJavaClass(
                    new File(destinationDirectory, "/" + one.getInternalName() + ".class"),
                    one.getData()
            );
        }

        for (GeneratorEntity one : entity.getGenerators()) {
            saveJavaClass(
                    new File(destinationDirectory, "/" + one.getInternalName() + ".class"),
                    one.getData()
            );
        }

        ModuleDumper dumper = new ModuleDumper(context, env, debug);
        dumper.setIncludeData(false);
        dumper.save(entity, new File(destinationDirectory, "/" + entity.getInternalName() + ".dump"));
    }

    private ModuleEntity compileFile(File path, File destinationDirectory) {
        try {
            ModuleEntity entity = env.getModuleManager().fetchModule(path.getPath());
            saveModuleClasses(entity, destinationDirectory);
            return entity;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public void compileJar(File jarFile, File destinationDirectory) throws IOException, InterruptedException {
        compileJar(jarFile, destinationDirectory, null);
    }

    public void compileJar(File jarFile, File destinationDirectory, String mainClassName) throws IOException, InterruptedException {
        Manifest manifest = null;

        if (mainClassName != null) {
            manifest = new Manifest();
            manifest.getMainAttributes().put(Attributes.Name.IMPLEMENTATION_TITLE, Information.NAME + " Compiler");
            manifest.getMainAttributes().put(Attributes.Name.IMPLEMENTATION_VENDOR, Information.NAME);
            manifest.getMainAttributes().put(Attributes.Name.IMPLEMENTATION_VERSION, Information.CORE_VERSION);
            manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, mainClassName);
            manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
            manifest.getMainAttributes().put(Attributes.Name.CLASS_PATH, "jphp-runtime.jar");
        }

        try (FileOutputStream outputStream = new FileOutputStream(jarFile)) {
            JarOutputStream jarOutputStream = manifest != null
                    ? new JarOutputStream(outputStream, manifest)
                    : new JarOutputStream(outputStream);

            try {
                scan(destinationDirectory, new Callback<Boolean, File>() {
                    @Override
                    public Boolean call(File file) {
                    Path relPath = destinationDirectory.toPath().relativize(file.toPath());
                    String name = relPath.toString().replace('\\', '/');

                    try {
                        if (file.isFile()) {
                            JarEntry jarEntry = new JarEntry(name);
                            jarEntry.setTime(file.lastModified());

                            jarOutputStream.putNextEntry(jarEntry);

                            try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
                                byte[] buffer = new byte[1024 * 32];
                                while (true) {
                                    int count = in.read(buffer);

                                    if (count == -1) {
                                        break;
                                    }

                                    jarOutputStream.write(buffer, 0, count);
                                }

                                jarOutputStream.closeEntry();
                            }

                        } else if (file.isDirectory()) {
                            JarEntry jarEntry = new JarEntry(name);
                            jarEntry.setTime(file.lastModified());

                            jarOutputStream.putNextEntry(jarEntry);
                            jarOutputStream.closeEntry();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return false;
                    }
                });
            } catch (InterruptedException e) {
                // nop.
            }

            jarOutputStream.close();
        }
    }

    public void compile(File destinationDirectory) {
        StandaloneLibraryDumper dumper = new StandaloneLibraryDumper();

        try {
            scan(sourceDirectory, new Callback<Boolean, File>() {
                @Override
                public Boolean call(File file) {
                    if (file.isFile()) {
                        String ext = FsUtils.ext(file.getPath());

                        if (ext != null) {
                            switch (ext) {
                                case "php":
                                case "phb":
                                    System.out.println("Compile: " + file);
                                    dumper.addModule(compileFile(file, destinationDirectory));
                                    break;
                                default:
                                    Path path = Paths.get(sourceDirectory.getPath());
                                    Path relPath = path.relativize(file.toPath());

                                    try {
                                        System.out.println("Copy: " + file);
                                        Path target = Paths.get(destinationDirectory.getPath(), "/", relPath.toString());

                                        target.toFile().getParentFile().mkdirs();

                                        Path copy = Files.copy(file.toPath(), target);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    break;
                            }
                        }
                    }

                    return false;
                }
            });
        } catch (InterruptedException e) {
            // nop.
        }

        File file = new File(destinationDirectory, "/JPHP-INF/library.dump");
        file.getParentFile().mkdirs();

        try {
            try (FileOutputStream output = new FileOutputStream(file)) {
                dumper.save(output);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void scan(File sourceDirectory, Callback<Boolean, File> callback) throws InterruptedException {
        File[] files = sourceDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                Boolean result = callback.call(file);

                if (result != null && result) {
                    throw new InterruptedException();
                }

                if (file.isDirectory()) {
                    scan(file, callback);
                }
            }
        }
    }
}
