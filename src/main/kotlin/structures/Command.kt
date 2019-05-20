package structures

import InterpretationState
import java.util.HashMap
import kotlin.system.exitProcess

/**
 * Represents command of debugger.
 */
enum class Command(val word: String, val description: String) : Word {

    STEP_IN("i", "step into the called procedure") {
        override fun action(state: InterpretationState): Boolean {
            state.stepIn = true
            return true
        }
    },

    STEP_OVER("o", "step over the called procedure") {
        override fun action(state: InterpretationState): Boolean {
            state.stepIn = false
            return true
        }
    },

    TRACE("trace", "print call stack") {
        override fun action(state: InterpretationState): Boolean {
            state.callStack.forEachIndexed { i, call ->
                val caller = state.callStack[i]
                state.outputHandler.writeString(
                        "${caller.returnLineNumber}:" +
                                " ${call.procedure.name}", i, true)
            }
            return false
        }
    },

    VAR("var", "print all existing variables with their values") {
        override fun action(state: InterpretationState): Boolean {
            state.variables.forEach {
                state.outputHandler.writeString(" ${it.key} = ${it.value.value}", newLine = true)
            }
            return false
        }
    },

    // custom commands go here

    HELP("help", "UH-OH! You surely know what this means.") {
        override fun action(state: InterpretationState): Boolean {
            state.outputHandler.writeString("Available commands are:", newLine = true)
            entries.forEach {
                if (it.value == this) return@forEach
                state.outputHandler.writeString(
                        "${it.key} - ${it.value.description}", newLine = true)
            }
            return false
        }
    },

    RUN("run", "run program without debugger") {
        override fun action(state: InterpretationState): Boolean {
            state.stepMode = false
            return true
        }
    },

    EXIT("exit", "exit interpreter") {
        override fun action(state: InterpretationState): Boolean {
            exitProcess(0)
        }
    };


    /**
     * Performs action for that command.
     * @return if interpreter can continue
     */
    abstract fun action(state: InterpretationState): Boolean

    companion object {
        private val entries = HashMap<String, Command>()

        init {
            for (command in values())
                entries[command.word] = command
        }

        /**
         * Get command by it's word
         * @return [Command] or null if there's no such word for command
         */
        fun getCommand(word: String): Command? = entries[word]
    }
}

