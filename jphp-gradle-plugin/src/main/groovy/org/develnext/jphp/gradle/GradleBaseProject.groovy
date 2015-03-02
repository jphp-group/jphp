package org.develnext.jphp.gradle

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer

class GradleBaseProject {
    def Project project;

    GradleBaseProject(Project project) {
        this.project = project
    }

    protected Configuration getOrCreateConfiguration(String name) {
        ConfigurationContainer container = project.configurations
        container.findByName(name) ?: container.create(name)
    }
}
