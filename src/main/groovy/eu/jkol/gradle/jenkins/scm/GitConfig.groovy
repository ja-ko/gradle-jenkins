package eu.jkol.gradle.jenkins.scm

import eu.jkol.gradle.jenkins.CodeGenerator
import eu.jkol.gradle.jenkins.ScriptedObject


class GitConfig implements ScmConfig {

    String url
    boolean enabled

    GitConfig() {

    }

    void url(String url) {
        this.url = url
    }

    @Override
    CodeGenerator generateJobDsl() {
        return new ScriptedObject().helpConfigure {
            git {
                remote {
                    url this.url
                }
            }
        }
    }

    @Override
    CodeGenerator generatePipelineDsl() {
        return null
    }
}
