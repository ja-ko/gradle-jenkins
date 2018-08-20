package eu.jkol.gradle.jenkins

class ScriptedObject implements CodeGenerator {
    static final INDENTATION = 4
    def notMappedTypes = [Integer, Boolean, Double, Float]
    protected methods = []

    def methodMissing(String name, def args) {
        def transformedArgs = []
        args.each {
            if( it instanceof Closure ) { transformedArgs << transformClosure(it) }
            else { transformedArgs << it }
        }
        methods << new ScriptedMethod(name, transformedArgs)
    }

    void mixin(CodeGenerator mixin) {
        methods << mixin
    }

    void clear() {
        methods.clear()
    }

    static protected ScriptedObject transformClosure(Closure input) {
        def nested = new ScriptedObject()
        input.delegate = nested
        input.resolveStrategy = Closure.DELEGATE_FIRST
        input(nested)
        return nested
    }

    String generateGroovy() {
        def result = new StringBuilder()
        methods.each  {
            result.append(it.generateGroovy())
            result.append('\n')
        }
        return result.toString().trim()
    }

    ScriptedObject helpConfigure(Closure config) {
        config.delegate = this
        config.resolveStrategy = Closure.DELEGATE_FIRST
        config(this)
        return this
    }

    protected class ScriptedMethod implements CodeGenerator {
        String name
        def args = []

        ScriptedMethod(String name, def args) {
            this.name = name
            this.args = args as Object[]
        }

        @Override
        String generateGroovy() {
            def sb = new StringBuilder()
            sb.append(this.name)
            if (this.args.length == 0)
                sb.append('()')
            else {
                this.args.each { arg ->
                    if (notMappedTypes.any { it.isAssignableFrom(arg.getClass()) }) {
                        sb.append(' ')
                        sb.append(arg)
                        sb.append(',')
                    } else if (arg instanceof String) {
                        sb.append(" '$arg',")
                    } else if (arg instanceof Map) {
                        arg.each { k, v ->
                            sb.append(" ${k}: '${v}',")
                        }
                    } else if (arg instanceof ScriptedObject) {
                        def script = arg.generateGroovy()
                        sb.append(" {\n")
                        sb.append(IndentationHelper.indent(script))
                        sb.append("},")
                    }
                }
            }
            return sb.toString().replaceAll(',\\w*\$', '')
        }
    }
}
