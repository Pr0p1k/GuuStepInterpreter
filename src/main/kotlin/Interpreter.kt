import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import kotlin.IllegalArgumentException


lateinit var inputHandler: InputHandler
lateinit var outputHandler: OutputHandler
lateinit var interpretationState: InterpretationState

/**
 * Here we go
 * I suppose that we can pass only one file with code to interpreter.
 * If args.length is bigger than 1, then length - 1 args are passed
 * I/O processed via [InputHandler] and [OutputHandler]
 * If key -g is passed. The program is going to run in GUI mode, but this is not implemented
 */
fun main(args: Array<String>) {
    initProgram(args)
    val lines = readFile(args.last())
    lines.forEachIndexed { i, line ->
        val words = getWords(line)
        if (words.isNotEmpty()) {
            val operator = parseOperator(words[0])
            if (operator.paramsNumber == words.size - 1) {
                // TODO decide an action
            } else {
                // TODO throw smth
            }

        }
    }
}

/**
 * Reads args and initializes IO or throws exceptions
 */
fun initProgram(args: Array<String>) {
    when (args.size) {
        1 -> {
            inputHandler = InputHandler()
            outputHandler = OutputHandler()
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
 * @return
 */
@Throws(IllegalArgumentException::class)
fun parseOperator(word: String): Operator {
    when (word) {
        "sub", "print", "call" -> return Operator(word, 1)
        "set" -> return Operator(word, 2)
        else -> throw IllegalArgumentException("Unknown operator \"$word\"")
    }
}
