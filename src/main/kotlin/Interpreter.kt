import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import kotlin.IllegalArgumentException

lateinit var state: InterpretationState

/**
 * Here we go
 * I suppose that we can pass only one file with code to interpreter.
 * If args.length is bigger than 1, then length - 1 args are passed
 * I/O processed via [InputHandler] and [OutputHandler]
 * If key -r is passed, the interpreter runs in normal (non-step) mode, so it simply completes
 * the Guu program. If program is recursive, [StackOverflowError] is expected
 * If key -g is passed, the program is going to run in GUI mode, but this is not implemented
 */
fun main(args: Array<String>) {
    state = InterpretationState()
    initProgram(args)
    val lines = readFile(args.last())
    // Here goes hoisting of procedure declarations
    hoistDeclarations(lines)

}

/**
 * Reads args and initializes IO or throws exceptions
 */
fun initProgram(args: Array<String>) {
    when (args.size) {
        1 -> {
            state.inputHandler = InputHandler()
            state.outputHandler = OutputHandler()
        }
        0 -> {
            throw IllegalArgumentException("Source code file not specified")
        }
        else -> {
            // parse args
        }
    }

}


/**
 * Method to read the file.
 * @return Stream of lines from the file
 */
fun readFile(fileName: String): Sequence<String> {
    val file = File(fileName)
    val reader = BufferedReader(FileReader(file))
    return reader.lineSequence()
}

/**
 * Gets the words separated by whitespaces
 */
fun getWords(line: String): List<String> = line
        .split(Regex("\\s+"))
        .filter { it.isNotEmpty() }

/**
 * Parse an operator from string (let "sub" be called an operator too)
 * @return [Operator] object with name(command) and params number for the operator
 */
@Throws(IllegalArgumentException::class)
fun parseOperator(word: String) = Operator.valueOf(word.toUpperCase())

/**
 * Performs hoisting of procedure declarations
 * and ads them into procedures map in the [state] via action in [Operator]
 */
@Throws(MalformedLineException::class)
fun hoistDeclarations(lines: Sequence<String>) {
    lines.forEachIndexed { i, line ->
        val words = getWords(line)
        if (words.isNotEmpty()) {
            val operator = parseOperator(words[0])
            if (operator == Operator.SUB) {
                if (words.size == 2) {
                    useOperator(operator, words, i + 1)
                } else
                    throw MalformedLineException("Wrong amount of parameters" +
                            " for operator ${operator.word} at line ${i + 1}")
            }
        }
    }
}

/**
 * Performs actions for that operator (adds to procedures list, variables list, etc.)
 * @param operator [Operator] itself
 * @param lineNumber number to add in the map of procedures
 */
fun useOperator(operator: Operator, params: List<String>,
                lineNumber: Int) = operator.action(state, params, lineNumber)
