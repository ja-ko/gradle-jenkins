package eu.jkol.gradle.jenkins.jobdsl

import org.gradle.api.Plugin
import org.gradle.api.Project

class JenkinsJobDslPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def task = project.tasks.create("jobdsl", JobDslTask)
    }
}
