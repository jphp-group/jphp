package org.develnext.jphp.gradle

import org.gradle.api.Project

class GradlePhpServerProject extends GradleBaseProject {
    def PhpServerGradleExtension config;

    GradlePhpServerProject(Project project) {
        super(project);
    }

    def update() {
        this.config  = project.phpServer;
    }

    def run() {

    }
}
