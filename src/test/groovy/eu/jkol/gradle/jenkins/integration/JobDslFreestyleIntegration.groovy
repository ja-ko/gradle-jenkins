package eu.jkol.gradle.jenkins.integration

import eu.jkol.gradle.jenkins.jobdsl.JenkinsJobDslPlugin
import eu.jkol.gradle.jenkins.jobs.JenkinsJobsPlugin
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import spock.lang.Specification

class JobDslFreestyleIntegration extends Specification {

    def "scm is added if configured"() {
        when:
        def projectUnderTest = configure {
            jenkins {
                scm {
                    git {
                        url "fake://url"
                        enabled = true
                    }
                }
                freestyleJobs {
                    test
                }
            }
        }

        then:
        projectUnderTest.jobdsl.generate() == """job('test') {
    scm {
        git {
            remote {
                url 'fake://url'
            }
        }
    }
}

"""
    }

    def "invocation is added if configured"() {
        when:
        def projectUnderTest = configure {
            tasks.create('build')
            jenkins {
                invocations {
                    build {
                       // task project.clean
                        task project.build
                    }
                }
                freestyleJobs {
                    test
                }
            }
        }
        then:
        projectUnderTest.jobdsl.generate() == """job('test') {
    steps {
        gradle {
            useWrapper true
            tasks ':build'
            switches '--no-daemon'
            switches '--no-parallel'
            switches '--no-build-cache'
        }
    }
}

"""
    }

    def "result collector is added if configured"() {
        when:
        def projectUnderTest = configure {
            apply plugin: 'checkstyle'
            jenkins {
                freestyleJobs {
                    test
                }
            }
        }
        projectUnderTest.jenkins.collectors.checkstyle.lateSetup(projectUnderTest) // because afterEvaluate is not invoked

        then:
        projectUnderTest.jobdsl.generate() == """job('test') {
    publishers {
        checkstyle {
            pattern('build/reports/checkstyle/*.xml')
        }
    }
}

"""
    }



    static Project configure(Closure config) {
        def project = ProjectBuilder.builder().build()
        project.pluginManager.apply(JenkinsJobDslPlugin)
        project.pluginManager.apply(JenkinsJobsPlugin)
        project.configure(project, config)
        return project
    }
}
