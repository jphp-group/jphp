package org.develnext.jphp.android.tools.gradle

import org.develnext.jphp.android.tools.AndroidApplicationCreator
import org.gradle.api.Plugin
import org.gradle.api.Project

class GradlePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def task = project.task("jphpAndroid") << {
            def androidAppCreator = new AndroidApplicationCreator();

            if (!project.hasProperty("phpSrcDirs")) {
                println("Please set `project.srcDirs`");
                return;
            }

            def phpSrcDirs = project.phpSrcDirs;

            for (phpSrcDir in phpSrcDirs) {
                androidAppCreator.addFile(project.file(phpSrcDir));
            }

            def appFile = new File(project.buildDir.path + "/application.jar");
            androidAppCreator.saveTo(appFile);

            println("Dex library jar has been compiled to: " + appFile);
        }

        task.dependsOn("build");
    }
}
