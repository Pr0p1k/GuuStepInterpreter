package structures

import InterpretationState
import java.util.HashMap

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
                state.outputHandler.writeString(call.procedure.name, i, true)
            }
            return false
        }
    },

    VAR("var", "print all existing variables with their values") {
        override fun action(state: InterpretationState): Boolean {
            state.outputHandler.writeString("Variables:", newLine = true)
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
    };


    /**
     * Performs action for that command.
     * @return if interpreter can continue
     */
    abstract fun action(state: InterpretationState): Boolean

    companion object {
        private val entries = HashMap<String, Command>()
        private var linesLeft = 0

        init {
            for (command in values())
                entries[command.word] = command
        }

        /**
         * Get command by it's word
         */
        fun getCommand(word: String): Command? = entries[word]
    }
}

