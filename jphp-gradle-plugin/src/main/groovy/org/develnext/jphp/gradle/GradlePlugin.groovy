package org.develnext.jphp.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import php.runtime.Information

class GradlePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def isAndroid = project.plugins.findPlugin("com.android.application") != null;

        if (!isAndroid) {
            project.plugins.apply("java");
        }

        project.extensions.create('php', PhpGradleExtension);

        GradlePhpProject phpProject = new GradlePhpProject(project);

        def compilePhpTask = project.task("compilePhp") << {
            phpProject.update();
            phpProject.compile();
        }

        def buildPhpTask = project.task("buildPhp") << {
            phpProject.update();
            phpProject.build();
        }

        def phpVersion = project.task("phpVersion") << {
            println "JPHP v" + Information.CORE_VERSION
            println "Like PHP v" + Information.LIKE_PHP_VERSION;
            println "Copyright " + Information.COPYRIGHT;
        }

        if (!isAndroid) {
            project.tasks.getByPath("compileJava").dependsOn(compilePhpTask);
        }

        buildPhpTask.dependsOn(compilePhpTask);
        project.tasks.getByPath("build").dependsOn(buildPhpTask);
    }
}
