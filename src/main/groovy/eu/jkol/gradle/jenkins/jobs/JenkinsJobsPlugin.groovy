package eu.jkol.gradle.jenkins.jobs

import org.gradle.api.Plugin
import org.gradle.api.Project

class JenkinsJobsPlugin implements Plugin<Project> {
    @Override
    void apply(Project target) {
        target.extensions.create("jenkins", JenkinsExtension, target)
    }
}
