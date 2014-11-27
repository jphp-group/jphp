package org.develnext.jphp.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class GradlePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.extensions.create('php', PhpGradleExtension);

        def compilePhpTask = project.task("compilePhp") << {
            GradlePhpProject phpProject = new GradlePhpProject(project);
            phpProject.compile();
        }

        def buildPhpTask = project.task("buildPhp") << {
            GradlePhpProject phpProject = new GradlePhpProject(project);
            phpProject.build();
        }

        def buildPortablePhp = project.task("buildPortablePhp") << {
            GradlePhpProject phpProject = new GradlePhpProject(project);
            phpProject.buildPortable();
        }

        buildPhpTask.dependsOn(compilePhpTask);
        buildPortablePhp.dependsOn(compilePhpTask);
    }
}
