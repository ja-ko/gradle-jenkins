package eu.jkol.gradle.jenkins.scm

import eu.jkol.gradle.jenkins.CodeGenerator

interface ScmConfig {
    CodeGenerator generateJobDsl()
    CodeGenerator generatePipelineDsl()
    boolean isEnabled()
}