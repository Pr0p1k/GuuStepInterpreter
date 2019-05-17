package io

/**
 * Processes the output of the interpreter default mode is console
 */
class OutputHandler(override val GUIMode: Boolean = false) : Handler {

    fun writeString(output: String) {
        if (!GUIMode) {
            println(output)
        } else {
            // Here optional gui goes
            throw NotImplementedError("GUI mode is not implemented")
        }
    }
}
