package eu.jkol.gradle.jenkins.jobs.jenkinsfile

import eu.jkol.gradle.jenkins.jobs.collectors.CheckstyleCollector
import eu.jkol.gradle.jenkins.jobs.collectors.ResultCollectors
import org.gradle.api.Project

import javax.inject.Inject

class JenkinsfileExtension {

    ResultCollectors resultCollectors

    @Inject
    JenkinsfileExtension(Project project) {
        resultCollectors = project.objects.newInstance(ResultCollectors)
        resultCollectors.extensions.create("checkstyle", CheckstyleCollector)
    }
}
