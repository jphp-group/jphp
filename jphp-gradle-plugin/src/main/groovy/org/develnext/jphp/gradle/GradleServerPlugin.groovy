package org.develnext.jphp.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class GradleServerPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        if (!project.plugins.hasPlugin("php")) {
            project.plugins.apply("php");
        }

        if (!project.plugins.hasPlugin("war")) {
            project.plugins.apply("war");
        }

        if (!project.plugins.hasPlugin("jetty")) {
            project.plugins.apply("jetty");
        }

        project.extensions.create('phpServer', PhpServerGradleExtension);

        GradlePhpServerProject phpServerProject = new GradlePhpServerProject(project);

        def phpServerRun = project.task("phpServerRun") << {
            phpServerProject.update();
            phpServerProject.run();
        }

        phpServerRun.dependsOn("compilePhp");

        project.tasks.getByPath("jettyRun").dependsOn(phpServerRun);
        project.tasks.getByPath("jettyRunWar").dependsOn(phpServerRun);
        project.tasks.getByPath("war").dependsOn(phpServerRun);
    }
}
