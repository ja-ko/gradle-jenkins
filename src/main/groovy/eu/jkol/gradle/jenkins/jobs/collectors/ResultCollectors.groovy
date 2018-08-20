package eu.jkol.gradle.jenkins.jobs.collectors

class ResultCollectors {

    void create(String name, Class<? extends ResultCollector> type, Object... args) {
        extensions.create(name, type, args)
    }

    def getCollectors() {
        def result = []
        extensions.schema.each { key, type ->
            def extension = extensions.findByName(key)
            if (extension instanceof ResultCollector) {
                result << extension
            }
        }
        return result
    }
}
