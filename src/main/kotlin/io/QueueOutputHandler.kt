package io

import structures.Color
import java.util.*
import java.util.stream.Stream

/**
 * Implemented only for tests to verify the program's output.
 * No colors/levels and newlines, only raw output
 */
class QueueOutputHandler(var debugQueue: Queue<String>, var outputQueue: Queue<String>) : OutputHandler() {

    override fun writeDebuggerString(output: String, level: Int, newLine: Boolean, color: Color) {
        debugQueue.add(output)
    }

    override fun writeString(output: String, level: Int, newLine: Boolean, color: Color) {
        outputQueue.add(output)
    }
}
