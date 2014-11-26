package org.develnext.jphp.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class GradlePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println "Hello world!";
    }
}
