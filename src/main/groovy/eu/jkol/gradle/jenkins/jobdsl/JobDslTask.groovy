package eu.jkol.gradle.jenkins.jobdsl

import eu.jkol.gradle.jenkins.jobs.JenkinsExtension
import eu.jkol.gradle.jenkins.jobs.JenkinsJobsPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class JobDslTask extends DefaultTask {



    @OutputFile
    File destination

    @TaskAction
    void generateJobDsl() {
        if(destination.exists()) {
            destination.delete()
        }
        destination.withWriter { writer ->
            writer.append(generate())
        }
    }

    String generate() {
        def sb = new StringBuilder()
        project.rootProject.allprojects {
            if(it.extensions.findByType(JenkinsExtension) != null) {
                def generators = it.jenkins.jobDslGenerators
                generators.each {
                    sb.append(it.generateGroovy())
                    sb.append('\n\n')
                }
            }
        }
        return sb.toString()
    }

}
