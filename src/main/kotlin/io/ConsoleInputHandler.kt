package io

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader

class ConsoleInputHandler : InputHandler() {
    override val reader: Reader = BufferedReader(InputStreamReader(System.`in`))
    // here null should be replaced with another if gui is implemented.
    // I wanted to use Reader.nullReader(), but Kotlin doesn't support Java 11 :(

    /**
     * Reads the input for debugger.
     * Difference from [getInput] is only for gui
     */
    override fun getDebuggerInput(): String = getInput()

    override fun getInput(): String = (reader as BufferedReader).readLine()
}
