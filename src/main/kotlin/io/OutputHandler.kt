package io

import structures.Color

/**
 * Processes the output of the interpreter. Default mode is console
 */
class OutputHandler(override val guiMode: Boolean = false) : Handler {

    fun writeString(output: String, level: Int = 0, newLine: Boolean = false, color: Color = Color.DEFAULT) {
        if (!guiMode) {
            for (i in 0 until level) print(" ")
            print("\u001B[${color.number}m$output\u001B[0m")
            if (newLine) println()
        } else {
            // Here optional gui goes
            throw NotImplementedError("GUI mode is not implemented")
        }
    }
}
