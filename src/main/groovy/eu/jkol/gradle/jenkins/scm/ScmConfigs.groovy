package eu.jkol.gradle.jenkins.scm

import javax.inject.Inject

class ScmConfigs {

    @Inject
    ScmConfigs() {
    }

    ScmConfig create(String name, Class<? extends ScmConfig> type, Object... args) {
        return this.extensions.create(name, type, args)
    }

    def getConfigs() {
        def result = []
        extensions.schema.each { key, type ->
            def extension = extensions.findByName(key)
            if (extension instanceof ScmConfig) {
                result << extension
            }
        }
        return result
    }


}
