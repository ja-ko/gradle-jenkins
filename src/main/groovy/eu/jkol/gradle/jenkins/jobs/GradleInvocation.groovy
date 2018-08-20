package eu.jkol.gradle.jenkins.jobs

import eu.jkol.gradle.jenkins.CodeGenerator
import eu.jkol.gradle.jenkins.ScriptedObject
import org.gradle.api.Project
import org.gradle.api.Task

import javax.inject.Inject

class GradleInvocation {
    def tasks = []
    def properties = []
    def arguments = []
    boolean useWrapper = true
    boolean useDaemon = false
    boolean useParallel = false
    boolean useBuildCache = false
    boolean useBuildScan = false

    String name

    private Project project

    @Inject
    GradleInvocation(String name, Project project) {
        this.name = name
        this.project = project
    }

    GradleInvocation task(Task task) {
        def taskN
        tasks << task.path
        return this
    }

    GradleInvocation property(String property, String value) {
        properties << "-P${property}=${value}"
        return this
    }

    GradleInvocation argument(String argument) {
        arguments << argument
        return this
    }

    CodeGenerator generateJobDsl() {
        return new ScriptedObject().helpConfigure {
            gradle {
                useWrapper(this.useWrapper)
                this.tasks.each {
                    owner.delegate.tasks(it)
                }
                this.properties.each {
                    owner.delegate.switches(it)
                }
                this.useDaemon ? switches('--daemon') : switches('--no-daemon')
                this.useParallel ? switches('--parallel') : switches('--no-parallel')
                this.useBuildCache ? switches('--build-cache') : switches('--no-build-cache')
                this.useBuildScan ? switches('--scan') : {}

            }
        }
    }

    CodeGenerator generatePipelineDsl() {

    }
}
