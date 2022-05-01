import java.io.File
import kotlin.system.exitProcess

val KNOW = System.getenv("HOME") + File.separator + "Wissen/KNOW"
const val LINE = "-------------------------------------------------------------------------------"


fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println(
            """
            Search for <pattern1> AND <patternI> in file ${KNOW}
            
            Usage: know <pattern1> ... <patternN>
            """.trimIndent()
        )

        exitProcess(1)
    }
    val pattern = mutableListOf<Regex>()
    args.forEach { pattern.add(Regex(it, RegexOption.IGNORE_CASE)) }
    var inside = false
    val lineSeparator = Regex("^---+")
    var text = ""
    File(KNOW).reader().forEachLine {
        if (lineSeparator.containsMatchIn(it)) {
            if (inside) {
                // we found the end marker
                search(text, pattern)
                inside = false
                text = ""
            }
            inside = true
        } else if (inside) {
            text = "$text\n$it"
        }
    }
}

fun search(text: String, pattern: MutableList<Regex>) {
    pattern.forEach {
        if (!it.containsMatchIn(text)) {
            return
        }
    }
    println(LINE)
    println(text)
}
