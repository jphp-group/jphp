package org.develnext.jphp.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class GradlePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def sourceSets = project.container(SourceSet);

        def phpSourceSet = sourceSets.find {
            it.name == "php"
        }

        if (phpSourceSet == null) {
            phpSourceSet = new SourceSet("php");
            phpSourceSet.srcDir = project.file('src/main/php');
        }
    }
}
