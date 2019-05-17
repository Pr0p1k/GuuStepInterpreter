package io

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader

class InputHandler(override val GUIMode: Boolean = false) : Handler {
    private val reader: Reader? = if (!GUIMode)
        BufferedReader(InputStreamReader(System.`in`))
    else null
    // here null should be replaced with another if gui is implemented.
    // I wanted to use Reader.nullReader(), but Kotlin doesn't support Java 11 :(

    fun getInput(): String {
        if (!GUIMode) {
            return (reader as BufferedReader).readLine()
        } else throw NotImplementedError("GUI input not implemented")
    }
}
