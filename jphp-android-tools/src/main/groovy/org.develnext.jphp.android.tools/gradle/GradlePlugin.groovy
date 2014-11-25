package org.develnext.jphp.android.tools.gradle

import org.develnext.jphp.android.tools.AndroidApplicationCreator
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.ModuleDependency

class GradlePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.buildscript.repositories {
            maven { url 'https://oss.sonatype.org/content/repositories/snapshots/' }
        }

        project.repositories {
            maven { url 'https://oss.sonatype.org/content/repositories/snapshots/' }
        }

        Configuration compileConfiguration      = getOrCreateConfiguration(project, "compile");
        Configuration compileBuildConfiguration = getOrCreateBuildConfiguration(project, "classpath");

        addDependency(project, 'org.develnext:jphp-android:0.5-SNAPSHOT', compileConfiguration);

        def task = project.task("buildPhpForAndroid") << {
            def androidAppCreator = new AndroidApplicationCreator();

            compileConfiguration.each {
                androidAppCreator.registerExtensionJar(it);
            }

            androidAppCreator.scope.extensions.findAll {
                println "Include extension: $it"
            }

            if (!project.hasProperty("phpSrcDirs")) {
                println "Please set `project.srcDirs`";
                return;
            }

            def phpSrcDirs = project.phpSrcDirs;

            for (phpSrcDir in phpSrcDirs) {
                androidAppCreator.addFile(project.file(phpSrcDir));
            }

            def appFile = new File(project.projectDir.path + "/libs/jphp.compiled.jar");

            if (!appFile.parentFile.exists()) {
                appFile.parentFile.mkdirs();
            }

            androidAppCreator.saveTo(appFile);

            println("Dex library jar has been compiled to: " + appFile);
        }

        project.tasks.getByPath("preBuild").dependsOn("buildPhpForAndroid");
    }


    protected static ModuleDependency addDependency(Project project, String notation, Configuration configuration) {
        ModuleDependency dependency = project.dependencies.create(notation) as ModuleDependency
        configuration.dependencies.add(dependency)
        dependency
    }

    protected static ModuleDependency addBuildDependency(Project project, String notation, Configuration configuration) {
        ModuleDependency dependency = project.buildscript.dependencies.create(notation) as ModuleDependency
        configuration.dependencies.add(dependency)
        dependency
    }

    protected static Configuration getOrCreateConfiguration(Project project, String name) {
        ConfigurationContainer container = project.configurations
        container.findByName(name) ?: container.create(name)
    }

    protected static Configuration getOrCreateBuildConfiguration(Project project, String name) {
        ConfigurationContainer container = project.buildscript.configurations
        container.findByName(name) ?: container.create(name)
    }
}
