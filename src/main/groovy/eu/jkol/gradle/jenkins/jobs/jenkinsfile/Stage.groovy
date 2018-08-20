package eu.jkol.gradle.jenkins.jobs.jenkinsfile

class Stage {
    String name

    Stage(String name) {
        this.name = name
    }

    void checkout() {

    }

    void invokeGradle(String arguments = "") {

    }
}
