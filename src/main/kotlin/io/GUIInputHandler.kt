package io

import java.io.Reader

class GUIInputHandler : InputHandler() {
    override val reader: Reader
        get() = throw NotImplementedError("GUI input not implemented")

    override fun getDebuggerInput(): String {
        throw NotImplementedError("GUI input not implemented")
    }

    override fun getInput(): String {
        throw NotImplementedError("GUI input not implemented")
    }
}
