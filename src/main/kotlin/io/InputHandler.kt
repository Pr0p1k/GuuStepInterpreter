package io

import java.io.Reader

/**
 * Processes the input of interpreter.
 */
abstract class InputHandler : Handler {
    abstract val reader: Reader


    abstract fun getDebuggerInput(): String

    abstract fun getInput(): String
}
