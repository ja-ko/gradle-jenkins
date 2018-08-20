package eu.jkol.gradle.jenkins.functional

import eu.jkol.gradle.jenkins.jobs.freestyle.FreestyleJob
import groovy.io.FileType
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Files

class FunctionalTest extends Specification {

    @Rule final TemporaryFolder projectDir = new TemporaryFolder()
    File buildFile


    @Unroll
    def "#testdir produces desired results"() {
        given:
        new File("src/test/resources/functional/$testdir").eachFileRecurse(FileType.FILES, {file ->
            if(file.name.startsWith('input')) {
                Files.copy(file.toPath(), projectDir.root.toPath().resolve(file.name.replace('input-', '')))
            }
        })

        when:
        def result = GradleRunner.create()
                .withProjectDir(projectDir.root)
                .withPluginClasspath()
                .withArguments(task)
                .withDebug(true).build()

        then:
        new File("src/test/resources/functional/$testdir").eachFileRecurse(FileType.FILES, {file ->
            if(file.name.startsWith('output')) {
                def actual = new File(projectDir.root, file.name.replace('output-', '')).text.replaceAll('\r\n', '\n')
                def expected = file.text.replaceAll('\r\n', '\n')
                assert actual == expected
            }
        })

        where:
        testdir                  | task
        'jobdsl/singleproject'   | 'jobdsl'
        'jobdsl/multiproject'    | 'jobdsl'
    }




}
