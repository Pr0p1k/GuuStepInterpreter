package io

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader

class InputHandler(override val guiMode: Boolean = false) : Handler {
    private val reader: Reader? = if (!guiMode)
        BufferedReader(InputStreamReader(System.`in`))
    else null
    // here null should be replaced with another if gui is implemented.
    // I wanted to use Reader.nullReader(), but Kotlin doesn't support Java 11 :(

    /**
     * Reads the input for debugger.
     * Difference from [getInput] is only for gui
     */
    fun getDebuggerInput(): String {
        if (!guiMode) {
            return (reader as BufferedReader).readLine()
        } else throw NotImplementedError("GUI input not implemented")
    }

    fun getInput(): String {
        if (!guiMode) {
            return (reader as BufferedReader).readLine()
        } else throw NotImplementedError("GUI input not implemented")
    }
}
