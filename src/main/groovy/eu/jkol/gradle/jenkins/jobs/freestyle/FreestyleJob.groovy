package eu.jkol.gradle.jenkins.jobs.freestyle

import eu.jkol.gradle.jenkins.CodeGenerator
import eu.jkol.gradle.jenkins.IndentationHelper
import eu.jkol.gradle.jenkins.ScriptedObject
import eu.jkol.gradle.jenkins.jobs.GradleInvocation
import eu.jkol.gradle.jenkins.jobs.collectors.ResultCollector
import eu.jkol.gradle.jenkins.jobs.collectors.ResultCollectors
import eu.jkol.gradle.jenkins.scm.ScmConfigs
import org.gradle.api.invocation.Gradle

import javax.inject.Inject

class FreestyleJob extends ScriptedObject {

    String name
    def invocations = []
    ResultCollectors collectors
    ScmConfigs scm

    @Inject
    FreestyleJob(String name, def invocations = [], ResultCollectors collectors = null, ScmConfigs scm = null) {
        this.name = name
        this.invocations = invocations
        this.collectors = collectors
        this.scm = scm
    }

    @Override
    String generateGroovy() {
        addToBlock("scm", scm?.configs?.findAll {it.enabled}?.collect {it.generateJobDsl()})
        addToBlock("steps", invocations.collect {it.generateJobDsl()})
        addToBlock("publishers", collectors?.collectors?.findAll  {it.enabled}?.collect {it.generateJobDsl()})


        def sb = new StringBuilder()
        sb.append("job('$name') {\n")
        sb.append(IndentationHelper.indent(super.generateGroovy()))
        sb.append("}")
        return sb.toString()
    }

    def addToBlock(String blockName, def generatorsToAdd) {
        if(generatorsToAdd == null) { return }
        if(generatorsToAdd.size() > 0 ) {
            def method = addMethodIfMissing(blockName)
            def block = method.args.find { it instanceof ScriptedObject } as ScriptedObject
            generatorsToAdd.each {
                if( it instanceof CodeGenerator) {block.mixin(it)}
            }
        }
    }

    def addMethodIfMissing(String name) {
        return methods.find {it.name == name} ?: {
            super."$name" {}
            return methods.last()
        }()
    }
}
