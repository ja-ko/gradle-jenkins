package eu.jkol.gradle.jenkins

class IndentationHelper {

    static DEFAULT_INDENTATION = 4

    static String indent(String text, int spaces = DEFAULT_INDENTATION) {
        def sb = new StringBuilder()
        text.eachLine {line ->
            (1..spaces).each {sb.append(' ')}
            sb.append("$line\n")
        }
        return sb.toString()
    }

}
