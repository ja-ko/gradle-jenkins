package eu.jkol.gradle.jenkins.jobs.collectors

import eu.jkol.gradle.jenkins.CodeGenerator
import eu.jkol.gradle.jenkins.IndentationHelper
import groovy.transform.PackageScope
import org.gradle.api.Project

class BaseResultCollector implements ResultCollector {
    def storage = [:]
    def propertyMissing(String name, value) { storage[name] = value }
    def propertyMissing(String name) { storage[name] }

    final String name
    String pluginName
    protected Project project
    boolean disabled = false

    BaseResultCollector(String name, Project project = null, String plugin = null) {
        this.name = name
        this.project = project
        this.pluginName = plugin
    }

    @Override
    boolean isEnabled() {
        !disabled && project?.pluginManager?.hasPlugin(pluginName)
    }

    @Override
    CodeGenerator generateJobDsl() {
        return {
            def sb = new StringBuilder()
            sb.append("$name {\n")

            def nested = new StringBuilder()
            storage.each { k, v ->
                nested.append("$k('$v')\n")
            }
            sb.append(IndentationHelper.indent(nested.toString()))
            sb.append("}\n")
            return sb.toString()
        }
    }

    @Override
    CodeGenerator generatePipelineDsl() {
        return {
            def sb = new StringBuilder()
            sb.append("$name")
            storage.each { k, v ->
                sb.append(" $k: ")
                if(v instanceof String || v instanceof GString)
                    sb.append("'$v'")
                else
                    sb.append(v)
                sb.append(",")
            }
            return "${sb.toString().replaceAll(',\\w*$', '')}\n"
        }
    }
}
