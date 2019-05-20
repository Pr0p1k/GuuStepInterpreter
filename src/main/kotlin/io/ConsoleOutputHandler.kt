package io

import structures.Color

class ConsoleOutputHandler : OutputHandler() {
    override fun writeDebuggerString(output: String, level: Int, newLine: Boolean, color: Color) =
            writeString(output, level, newLine, color)

    override fun writeString(output: String, level: Int, newLine: Boolean, color: Color) {
        for (i in 0 until level) print("\t")
        print("\u001B[${color.number}m$output\u001B[0m")
        if (newLine) println()
    }
}
