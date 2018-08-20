package eu.jkol.gradle.jenkins.jobs.collectors

import eu.jkol.gradle.jenkins.CodeGenerator

interface ResultCollector {
    boolean isEnabled()
    CodeGenerator generateJobDsl()
    CodeGenerator generatePipelineDsl()
}