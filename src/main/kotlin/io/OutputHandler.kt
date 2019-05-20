package io

import structures.Color

/**
 * Processes the output of the interpreter.
 */
abstract class OutputHandler : Handler {


    abstract fun writeDebuggerString(output: String, level: Int = 0, newLine: Boolean = false,
                                     color: Color = Color.DEFAULT)

    abstract fun writeString(output: String, level: Int = 0, newLine: Boolean = false,
                             color: Color = Color.DEFAULT)
}
