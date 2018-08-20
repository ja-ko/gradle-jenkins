package eu.jkol.gradle.jenkins.unit

import eu.jkol.gradle.jenkins.ScriptedObject
import spock.lang.Specification


class ScriptedObjectUnit extends Specification {

    def systemUnderTest = new ScriptedObject()

    def "can record methods"() {
        when:
        systemUnderTest.someMethod()
        systemUnderTest.someOtherMethod()
        systemUnderTest.aThirdMethod()

        then:
        systemUnderTest.generateGroovy() == """someMethod()
someOtherMethod()
aThirdMethod()"""
    }

    def "can handle arguments"() {
        when:
        systemUnderTest.haveSomeArgs "string", true, 17

        then:
        systemUnderTest.generateGroovy() == "haveSomeArgs 'string', true, 17"
    }

    def "can handle maps"() {
        when:
        systemUnderTest.whatAboutMaps can:"it", really:"work"

        then:
        systemUnderTest.generateGroovy() == "whatAboutMaps can: 'it', really: 'work'"
    }


    def "can handle closures"() {
        when:
        systemUnderTest.andNestIt {
            evenFurther()
        }

        then:
        systemUnderTest.generateGroovy() == """andNestIt {
    evenFurther()
}"""
    }

    def "can handle complex objects"() {
        setup:
        def definition = {
            doSomething()
            withArguments "test", {
                someNested {
                    nestingItFurther {
                        whereAreWe()
                    }
                    letsContinueHere with: "Maps"
                }
            }
        }
        definition.delegate = systemUnderTest

        when:
        definition()

        then:
        systemUnderTest.generateGroovy() == """doSomething()
withArguments 'test', {
    someNested {
        nestingItFurther {
            whereAreWe()
        }
        letsContinueHere with: 'Maps'
    }
}"""

    }

    def "can mixin other ScriptedObjects"() {
        setup:
        def mixin = new ScriptedObject()
        mixin.letsMixItIn()

        when:
        systemUnderTest.doSomething()
        systemUnderTest.mixin(mixin)
        systemUnderTest.somethingElse {
            delegate.mixin(mixin)
        }

        then:
        systemUnderTest.generateGroovy() == """doSomething()
letsMixItIn()
somethingElse {
    letsMixItIn()
}"""
    }
}
