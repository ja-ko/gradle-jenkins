package eu.jkol.gradle.jenkins.unit

import eu.jkol.gradle.jenkins.CodeGenerator
import eu.jkol.gradle.jenkins.jobs.GradleInvocation
import eu.jkol.gradle.jenkins.jobs.collectors.ResultCollector
import eu.jkol.gradle.jenkins.jobs.collectors.ResultCollectors
import eu.jkol.gradle.jenkins.jobs.freestyle.FreestyleJob
import eu.jkol.gradle.jenkins.scm.ScmConfig
import eu.jkol.gradle.jenkins.scm.ScmConfigs
import spock.lang.Specification

class FreetsyleUnit extends Specification {


    def "adds invocations with no custom steps"() {
        given: "a freestylejob with a configured gradle invocation"
        def systemUnderTest = createSystemUnderTestWithInvocations()

        when: "nothing is configured"

        then: "a step for the invocation is added"
        systemUnderTest.generateGroovy() == """job('freestyle') {
    steps {
        FINDME
    }
}"""
    }

    def "adds invocations with custom steps"() {
        given: "a freestylejob with a configured gradle invocation"
        def systemUnderTest = createSystemUnderTestWithInvocations()

        when: "the user adds some own steps"
            systemUnderTest.steps {
                doSomePreperation()
            }
        then: "the generated code contains the custom steps as well as the gradle invocation"
        systemUnderTest.generateGroovy() == """job('freestyle') {
    steps {
        doSomePreperation()
        FINDME
    }
}"""
    }

    def "adds result collectors if configured"() {
        given: "a freestylejob with configured result collectors"
        def resultCollector = Mock(ResultCollector)
        def collectors = Mock(ResultCollectors)
        collectors.collectors >> [resultCollector]
        resultCollector.enabled >> true
        resultCollector.generateJobDsl() >> Mock(CodeGenerator)
        resultCollector.generateJobDsl().generateGroovy() >> "FINDME"
        def systemUnderTest = new FreestyleJob("test", [], collectors)

        when: "nothing is configured"

        then: "the result collector is added"
        systemUnderTest.generateGroovy() == """job('test') {
    publishers {
        FINDME
    }
}"""
    }

    def "adds scm if configured"() {
        given: "a freestylejob with configured scm"
        def scm = Mock(ScmConfig)
        def scmConfigs = Mock(ScmConfigs)
        scmConfigs.configs >> [scm]
        scm.enabled >> true
        scm.generateJobDsl() >> Mock(CodeGenerator)
        scm.generateJobDsl().generateGroovy() >> "FINDME"
        def systemUnderTest = new FreestyleJob("test", [], null, scmConfigs)

        when: "nothing is configured"

        then: "the scm is added"
        systemUnderTest.generateGroovy() == """job('test') {
    scm {
        FINDME
    }
}"""
    }



    FreestyleJob createSystemUnderTestWithInvocations() {
        def invocations = []
        def invocation = Mock(GradleInvocation)
        def invocationCode = Mock(CodeGenerator)
        invocationCode.generateGroovy() >> "FINDME"
        invocation.generateJobDsl() >> invocationCode
        invocations << invocation

        return new FreestyleJob("freestyle", invocations)
    }
}
