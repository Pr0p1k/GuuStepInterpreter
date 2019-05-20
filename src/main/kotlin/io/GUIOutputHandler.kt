package io

import structures.Color

class GUIOutputHandler : OutputHandler() {

    override fun writeDebuggerString(output: String, level: Int, newLine: Boolean, color: Color) {
        throw NotImplementedError("GUI output is not implemented")
    }

    override fun writeString(output: String, level: Int, newLine: Boolean, color: Color) {
        throw NotImplementedError("GUI output is not implemented")
    }
}
