package structures

import InterpretationState
import exceptions.MalformedLineException
import exceptions.ProcedureNotFoundException
import exceptions.RedeclarationException

/**
 * Represents all the operators of the language
 * [SUB] and [CALL] are some kind of special operators,
 * so somewhere they should be checked to do additional things
 */
enum class Operator(val word: String, private val paramsLength: Int) {
    SUB("sub", 1) {
        override fun action(state: InterpretationState, params: List<String>, lineNumber: Int) {
            checkParamsLength(params)
            if (state.procedures.containsKey(params[0]))
                throw RedeclarationException("structures.Procedure \"${params[0]}\" is already" +
                        " declared at line ${state.procedures[params[0]]!!.lineNumber}")
            state.procedures[params[0]] = Procedure(lineNumber, params[0])
            if (params[0] == "main") state.currentLine = lineNumber
        }
    },

    CALL("call", 1) {
        override fun action(state: InterpretationState, params: List<String>, lineNumber: Int) {
            checkParamsLength(params)
            state.callStack.push(Pair(state.procedures[params[0]]
                    ?: throw ProcedureNotFoundException(params[0]), lineNumber))
        }
    },

    SET("set", 2) {
        override fun action(state: InterpretationState, params: List<String>, lineNumber: Int) {
            checkParamsLength(params)
            state.variables[params[0]] = params[1].toInt()
        }
    },

    PRINT("print", 1) {
        override fun action(state: InterpretationState, params: List<String>, lineNumber: Int) {
            checkParamsLength(params)
            state.outputHandler.writeString("${params[0]} = ${state.variables[params[0]]}")
        }
    },

    // here your custom operators can be declared

    INC("inc", 1) {
        override fun action(state: InterpretationState, params: List<String>, lineNumber: Int) {
            checkParamsLength(params)
            state.variables[params[0]] = (state.variables[params[0]] ?: 0) + 1
        }
    },

    READ("read", 1) {
        override fun action(state: InterpretationState, params: List<String>, lineNumber: Int) {
            checkParamsLength(params)
            state.variables[params[0]] = state.inputHandler.getInput().toInt()
        }
    };

    abstract fun action(state: InterpretationState, params: List<String>, lineNumber: Int)

    fun checkParamsLength(params: List<String>) {
        if (params.size != paramsLength)
            throw MalformedLineException("Wrong amount of parameters for operator $word")
    }
}
