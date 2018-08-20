package eu.jkol.gradle.jenkins.jobs.jenkinsfile

import org.gradle.api.Plugin
import org.gradle.api.Project

class JenkinsfilePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def jenkinsfile = project.extensions.create("jenkinsfile", JenkinsfileExtension, project.objects)
        def task = project.tasks.create("createJenkinsfile", JenkinsfileTask)

    }
}
