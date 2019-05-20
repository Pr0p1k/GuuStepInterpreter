import exceptions.MalformedLineException
import io.InputHandler
import io.OutputHandler
import structures.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import kotlin.IllegalArgumentException
import kotlin.system.exitProcess

lateinit var state: InterpretationState

/**
 * Here we go
 * I suppose that we can pass only one file with code to interpreter.
 * If args.length is bigger than 1, then length - 1 options are passed
 * I/O processed via [InputHandler] and [OutputHandler]
 * If option -r is passed, the interpreter runs in normal (non-step) mode, so it simply completes
 * the Guu program. If program has recursion, loop is expected since there's no conditions
 * (and program will eventually die when callstack exceeds Integer.maxValue)
 * If option -g is passed, the program is going to run in GUI mode, but this is not implemented
 */
fun main(args: Array<String>) {
    state = InterpretationState()
    try {
        // read args and init I/O and mode
        initInterpreter(args)
        // read Guu file (file extension is not constrained)
        val lines = readFile(args.last()).toList()
        // hoist procedure declarations
        hoistDeclarations(lines)
        // add trees into procedures
        loadProcedureBodies(lines)      // тут я мог не загружать строки в деревья процедурам,
        // а читать их на ходу, но это задел на потенциальную возможность ветвления
        // put main into stack
        initProgram()
        // run interpretation
        if (state.stepMode)
            state.outputHandler.writeDebuggerString(
                    "Type help for the params of commands",
                    color = Color.GREEN, newLine = true)
        startInterpreter()
    } catch (e: IllegalArgumentException) {
        // IllegalArgumentException may be thrown when output is not yet initialized
        System.err.println(e.message)
        exitProcess(1)
    } catch (e: IOException) {
        System.err.println(e.message)
        exitProcess(1)
    } catch (e: Throwable) {
        state.outputHandler.writeDebuggerString(e.message ?: "Error occurred", color = Color.RED)
    }
}

/**
 * Reads args and initializes IO or throws [IllegalArgumentException]
 */
fun initInterpreter(args: Array<String>) {
    var gui = false
    when (args.size) {
        0 -> throw IllegalArgumentException("Source code file not specified")
        else -> for (i in 0 until args.lastIndex) {
            if (args[i].startsWith("-"))
                args[i].removePrefix("-").forEach {
                    when (it) {
                        'r' -> state.stepMode = false
                        'g' -> gui = true
                        else -> throw IllegalArgumentException("Unknown option \"$it\"")
                    }
                }
        }
    }
    state.inputHandler = InputHandler(gui)
    state.outputHandler = OutputHandler(gui)
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
@Throws(MalformedLineException::class)
fun parseOperator(word: String) =
        Operator.getOperator(word) ?: throw MalformedLineException("Illegal operator \"$word\"")

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
                    operator.action(state,
                            words.subList(1, words.size).map { Param(it) },
                            i + 1)
                else
                    throw MalformedLineException("Wrong amount of parameters" +
                            " for operator ${operator.word}")
        }
    }
}

/**
 * Loads a procedure body's lines into tree
 * Currently tree is not really needed, but if operators with block scopes are implemented,
 * tree would be good for performance
 */
fun loadProcedureBodies(lines: List<String>) {
    val sortedList = state.procedures.entries.sortedBy {
        it.value.lineNumber
    }
    sortedList.forEachIndexed { i, procedure ->
        for (j in procedure.value.lineNumber until if (i != sortedList.lastIndex)
            sortedList[i + 1].value.lineNumber - 1 else lines.size) {
            if (lines[j].isNotEmpty()) {
                val words = getWords(lines[j])
                val operator = Operator.getOperator(words[0])!!
                procedure.value
                        .tree.children.add(
                        SyntaxTreeNode(
                                Line(operator,
                                        words.map { Param(it) }.subList(1, words.size)
                                )))
            }
        }
    }
}

/**
 * Loads main procedure into stack or
 * @throws NoSuchMethodError
 */
@Throws(NoSuchMethodError::class)
fun initProgram() {
    Operator.CALL.action(state, listOf(Param("main")), state.procedures["main"]?.lineNumber
            ?: throw NoSuchMethodError("structures.Procedure \"main\" is not declared")
    )
}

/**
 * Builds string from [Line] with whitespaces as separators
 */
fun normalizeLine(line: Line): String =
        "${line.operator.word} " + line.params.map { it.value }.reduce { acc, s -> "$acc $s" }

/**
 * Starts interpreter at main procedure
 */
fun startInterpreter() {
    var localLineNumber = 0
    stack@ while (state.callStack.isNotEmpty()) {
        val procedure = state.callStack.peek().procedure
        for (i in localLineNumber until procedure.tree.children.size) {
            val operator = procedure.tree.children[i].value.operator
            val line = normalizeLine(procedure.tree.children[i].value)
            // here it checks whether to continue or wait for user's input
            if (state.stepMode)
                waitForCommand("${procedure.name}: ${procedure.lineNumber + i}: " + line)
            operator.action(state,
                    procedure.tree.children[i].value.params,
                    procedure.lineNumber + i + 1)
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
    if (state.callStack.peek().stepIn) {
        if (printLine) state.outputHandler.writeDebuggerString(line,
                state.callStack.size - 1, true, Color.MAGENTA)

        state.outputHandler.writeDebuggerString("Input command: ", color = Color.GREEN)

        val command = Command.getCommand(state.inputHandler.getDebuggerInput())
        if (command != null) {
            if (!command.action(state)) waitForCommand(line, false)
        } else {
            state.outputHandler.writeDebuggerString(
                    "Unknown command, type \"help\" for commands", newLine = true)
            waitForCommand(line, false)
        }
    }
}
