package org.develnext.jphp.gradle

import org.develnext.jphp.core.compiler.jvm.JvmCompiler
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import php.runtime.env.CompileScope
import php.runtime.env.Context
import php.runtime.env.Environment
import php.runtime.ext.support.Extension
import php.runtime.reflection.ModuleEntity
import php.runtime.reflection.support.Entity

import java.nio.charset.Charset
import java.util.zip.ZipFile

class GradlePhpProject {
    final Project project;
    final PhpGradleExtension config;

    final File buildDir;
    final File classesBuildDir;
    final File libsBuildDir;
    final File resourcesBuildDir;
    final File tmpBuildDir;

    List<File> includePaths;

    final Set<String> extensions;

    final CompileScope compileScope;

    GradlePhpProject(Project project) {
        this.project = project;
        this.config  = project.php;

        includePaths    = [project.file(config.srcDir)];
        buildDir        = project.buildDir;

        classesBuildDir   = new File(buildDir, "/classes");
        libsBuildDir      = new File(buildDir, "/libs");
        resourcesBuildDir = new File(buildDir, "/resources");
        tmpBuildDir       = new File(buildDir, "/tmp");

        extensions        = [] as HashSet;

        config.extensions.each {
            extensions.add(it.toLowerCase());
        }

        compileScope = new CompileScope();
    }

    /**
     * @return
     */
    def compile() {
        cleanClasses();

        registerExtensionsJars();

        def env = new Environment(compileScope);

        def classFiles = [] as List<File>;

        eachSourceFile { File file ->
            def context     = new Context(file, Charset.forName(config.charset));
            def compiler    = new JvmCompiler(env, context);
            def module      = compiler.compile(false);

            classFiles.addAll(writeToClasses(module));
        }

        def phpClassesFile = new File(tmpBuildDir, '/$php_classes.list');

        if (phpClassesFile.parentFile != null && !phpClassesFile.parentFile.exists()) {
            phpClassesFile.parentFile.mkdirs();
        }

        def writer = new PrintWriter(phpClassesFile);

        try {
            classFiles.each { writer.write(it.path + "\n"); }
        } finally {
            writer.close();
        }
    }

    def build() {

    }

    def buildPortable() {

    }

    def protected cleanClasses() {
        def phpClassesFile = new File(tmpBuildDir, '/$php_classes.list');

        if (phpClassesFile.isFile()) {
            def scanner = new Scanner(phpClassesFile);

            while (scanner.hasNextLine()) {
                def file = new File(scanner.nextLine());

                if (file.isFile()) {
                    file.delete();
                    file.deleteOnExit();
                }

                def list = file.parentFile != null ? file.parentFile.listFiles() : null;

                if (file.parentFile != null && list != null && list.length == 0) {
                    file.parentFile.delete();
                    file.parentFile.deleteOnExit();
                }
            }
        }
    }

    def protected getClassFileOfEntity(Entity entity) {
        def file = new File(classesBuildDir, "/" + entity.internalName + ".class");

        if (file.parentFile != null && !file.parentFile.exists()) {
            file.parentFile.mkdirs();
        }

        return file;
    }

    def File writeEntityToClassFile(Entity entity) {
        def file = getClassFileOfEntity(entity);
        def out  = new FileOutputStream(file, false);

        println "   Compile entity '${entity.internalName}' to '${file.path}'"

        try {
            out.write(entity.data);
        } finally {
            out.close();
        }

        return file;
    }

    def List<File> writeToClasses(ModuleEntity module) {
        def result = new ArrayList<File>();

        result.add(writeEntityToClassFile(module));

        for (el in module.classes) {
            result.add(writeEntityToClassFile(el));
        }

        for (el in module.functions) {
            result.add(writeEntityToClassFile(el));
        }

        for (el in module.closures) {
            result.add(writeEntityToClassFile(el));
        }

        for (el in module.generators) {
            result.add(writeEntityToClassFile(el));
        }

        return result;
    }

    def protected registerExtensionsJars() {
        Configuration compileConfiguration = getOrCreateConfiguration("compile");

        compileConfiguration.each {
            registerExtensionJar(it);
        }
    }

    def protected registerExtensionJar(File jarFile) {
        def zipFile = new ZipFile(jarFile, ZipFile.OPEN_READ);
        def entry   = zipFile.getEntry("JPHP-INF/extensions.list");

        if (entry != null) {
            Scanner scanner = new Scanner(zipFile.getInputStream(entry));
            while (scanner.hasNext()) {
                String line = scanner.nextLine().trim();

                if (!line.isEmpty()) {
                    try {
                        Extension extension = (Extension) Class.forName(line).newInstance();
                        compileScope.registerExtension(extension);
                    } catch (ClassNotFoundException e) {
                        // nop.
                    }
                }
            }
        }

        zipFile.close();
    }

    def protected eachSourceFile(Closure c) {
        eachSourceFile(c, includePaths);
    }

    def protected eachSourceFile (Closure c, File path) {
        eachSourceFile(c, path.listFiles().findAll());
    }

    def protected eachSourceFile (Closure c, Collection<File> paths) {
        paths.each {

            if (it.isDirectory()) {
                eachSourceFile(c, it);
            } else {
                def ext = it.path.substring(it.path.lastIndexOf('.') + 1).toLowerCase();

                print "Process file: ${it.path}";
                println " - ext: $ext";

                if (extensions.contains(ext)) {
                    c.call(it);
                    println " ... DONE"
                } else {
                    println " ... SKIP";
                }

                println();
            }
        }
    }

    protected Configuration getOrCreateConfiguration(String name) {
        ConfigurationContainer container = project.configurations
        container.findByName(name) ?: container.create(name)
    }
}
