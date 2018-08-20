package eu.jkol.gradle.jenkins.jobs

import eu.jkol.gradle.jenkins.CodeGenerator
import eu.jkol.gradle.jenkins.jobs.collectors.CheckstyleCollector
import eu.jkol.gradle.jenkins.jobs.collectors.ResultCollectors
import eu.jkol.gradle.jenkins.jobs.freestyle.FreestyleJob
import eu.jkol.gradle.jenkins.scm.GitConfig
import eu.jkol.gradle.jenkins.scm.ScmConfigs
import org.gradle.api.Project

class JenkinsExtension {
    def project

    JenkinsExtension(Project project) {
        this.project = project

        def invocations = project.container(GradleInvocation, {
            name -> project.objects.newInstance(GradleInvocation, name, project)
        })
        this.extensions.invocations = invocations

        def collectors = this.extensions.create('collectors', ResultCollectors)
        collectors.create('checkstyle', CheckstyleCollector, project)

        def scm = this.extensions.create('scm', ScmConfigs)
        scm.create('git', GitConfig)

        def freestyleJobs = project.container(FreestyleJob, {
            name -> project.objects.newInstance(FreestyleJob, name, invocations, collectors, scm)
        })
        this.extensions.freestyleJobs = freestyleJobs

    }

    List<CodeGenerator> getJobDslGenerators() {
        def result = []
        result.addAll(this.extensions.freestyleJobs)
        return result
    }

}
