import exceptions.MalformedLineException
import io.InputHandler
import io.OutputHandler
import structures.Colors
import structures.Command
import structures.Operator
import structures.SyntaxTreeNode
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
 * the Guu program. If program is recursive, [StackOverflowError] is expected since there's no conditions
 * If key -g is passed, the program is going to run in GUI mode, but this is not implemented
 */
fun main(args: Array<String>) {
    state = InterpretationState()
    initInterpreter(args)
    val lines = readFile(args.last()).toList()
    // Here goes hoisting of procedure declarations
    hoistDeclarations(lines)
    // Here add trees into procedures
    loadProcedureBodies(lines)
    // put main into stack
    initProgram()
    // run interpretation
    startInterpreter()
}

/**
 * Reads args and initializes IO or throws exceptions
 */
fun initInterpreter(args: Array<String>) {
    when (args.size) {
        1 -> {
            state.inputHandler = InputHandler()
            state.outputHandler = OutputHandler()
        }
        0 -> {
            throw IllegalArgumentException("Source code file not specified")
        }
        else -> {
            // TODO parse args
        }
    }

}


/**
 * Method to read the file.
 * @return Sequence of lines from the file
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
fun hoistDeclarations(lines: List<String>) {
    lines.forEachIndexed { i, line ->
        val words = getWords(line)
        if (words.isNotEmpty()) {
            val operator = parseOperator(words[0])
            if (operator == Operator.SUB)
                if (words.size == 2)
                    operator.action(state, words.subList(1, words.size), i + 1)
                else
                    throw MalformedLineException("Wrong amount of parameters" +
                            " for operator ${operator.word}")
        }
    }
}

/**
 * Loads procedure body's lines into tree
 * Currently tree is not really needed, but if operators with block scopes will be implemented,
 * tree will be good for performance
 */
fun loadProcedureBodies(lines: List<String>) {
    val sortedList = state.procedures.entries.sortedBy {
        it.value.lineNumber
    }
    // procedures don't have body's beginning and closing words,
    // so I load strings into tree eagerly
    sortedList.forEachIndexed { i, procedure ->
        for (j in procedure.value.lineNumber until if (i != sortedList.lastIndex)
            sortedList[i + 1].value.lineNumber - 1 else lines.size) {
            if (lines[j].isNotEmpty())
                procedure.value.tree.children.add(SyntaxTreeNode(lines[j]))
        }
    }
}

/**
 * Loads main procedure into stack or
 * @throws NoSuchMethodError
 */
@Throws(NoSuchMethodError::class)
fun initProgram() {
    Operator.CALL.action(state, listOf("main"), state.procedures["main"]?.lineNumber
            ?: throw NoSuchMethodError("structures.Procedure \"main\" is not declared")
    )
}

/**
 * Starts interpreter at main procedure
 */
fun startInterpreter() {
    var localLineNumber = 0
    stack@ while (state.callStack.isNotEmpty()) {
        val procedure = state.callStack.peek().procedure
        for (i in localLineNumber until procedure.tree.children.size) {
            val words = getWords(procedure.tree.children[i].value)
            val operator = parseOperator(words[0])
            // here it checks whether to continue or wait for user's input
            waitForCommand("${procedure.lineNumber + i}: " +
                    procedure.tree.children[i].value.trimStart())
            operator.action(state, words.subList(1, words.size), procedure.lineNumber + i + 1)
            if (operator == Operator.CALL) {
                continue@stack
            }
        }
        if (state.callStack.size > 1)
            localLineNumber = state.callStack.pop()
                    .returnLineNumber - state.callStack.peek().procedure.lineNumber
        else state.callStack.pop()
    }
}

/**
 * Waits for user to input command or continues if pass-through mode is enabled
 */
fun waitForCommand(line: String, printLine: Boolean = true) {
    if (state.stepMode && state.callStack.peek().stepIn) {
        if (printLine) state.outputHandler.writeString(line,
                state.callStack.size - 1, true, Colors.MAGENTA)
        state.outputHandler.writeString("Input command: ", color = Colors.GREEN)
        val command = Command.getCommand(state.inputHandler.getDebuggerInput())
        if (command != null) {
            if (!command.action(state)) waitForCommand(line, false)
        } else {
            state.outputHandler.writeString(
                    "Unknown command, type \"help\" for commands", newLine = true)
            waitForCommand(line, false)
        }
    }
}
