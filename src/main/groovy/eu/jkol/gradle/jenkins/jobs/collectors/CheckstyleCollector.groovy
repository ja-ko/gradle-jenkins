package eu.jkol.gradle.jenkins.jobs.collectors

import org.gradle.api.Project

class CheckstyleCollector extends BaseResultCollector {
    CheckstyleCollector(Project project) {
        super("checkstyle", project, "checkstyle")
        project.afterEvaluate {
            lateSetup(it)
        }
    }

    void lateSetup(Project it) {
        if (enabled && this.pattern == null)
            this.pattern = "${it.rootProject.relativePath(it.checkstyle.reportsDir)}/*.xml".replaceAll('\\\\','/')
    }


}
