package structures

import InterpretationState
import exceptions.MalformedLineException
import exceptions.ProcedureNotFoundException
import exceptions.RedeclarationException
import java.util.HashMap

/**
 * Represents all the operators of the language
 * [SUB] and [CALL] are some kind of special operators,
 * so somewhere they should be checked to do additional things
 */
enum class Operator(val word: String, private val paramsLength: Int) {
    SUB("sub", 1) {
        override fun action(state: InterpretationState, params: List<Param>, lineNumber: Int) {
            val name = params[0].value
            checkParamsLength(params)
            if (state.procedures.containsKey(name))
                throw RedeclarationException("Procedure \"$name\" is already" +
                        " declared at line ${state.procedures[name]!!.lineNumber}")
            state.procedures[params[0].value] = Procedure(lineNumber, params[0].value)
        }
    },

    CALL("call", 1) {
        override fun action(state: InterpretationState, params: List<Param>, lineNumber: Int) {
            checkParamsLength(params)
            state.callStack.push(StackFrame(state.procedures[params[0].value]
                    ?: throw ProcedureNotFoundException(params[0].value, lineNumber), lineNumber,
                    state.stepIn))
        }
    },

    SET("set", 2) {
        override fun action(state: InterpretationState, params: List<Param>, lineNumber: Int) {
            checkParamsLength(params)
            state.variables[params[0].value] = Value(params[1].value.toInt())
        }
    },

    // If variable is not specified, write empty string like bash does
    PRINT("print", 1) {
        override fun action(state: InterpretationState, params: List<Param>, lineNumber: Int) {
            checkParamsLength(params)
            state.outputHandler.writeString(
                    "${params[0].value} = ${state.variables[params[0].value]?.value ?: ""}",
                    newLine = true, color = Color.BLUE)
        }
    },

    // here your custom operators can be declared

    INC("inc", 1) {
        override fun action(state: InterpretationState, params: List<Param>, lineNumber: Int) {
            checkParamsLength(params)
            // Let's be like JS and work normally with an undeclared variable
            if (!state.variables.containsKey(params[0].value))
                state.variables[params[0].value] = Value(1)
            state.variables[params[0].value]!!.value++
        }
    },

    READ("read", 1) {
        override fun action(state: InterpretationState, params: List<Param>, lineNumber: Int) {
            checkParamsLength(params)
            state.variables[params[0].value] = Value(state.inputHandler.getDebuggerInput().toInt())
        }
    };

    abstract fun action(state: InterpretationState, params: List<Param>, lineNumber: Int)

    fun checkParamsLength(params: List<Param>) {
        if (params.size != paramsLength)
            throw MalformedLineException("Wrong amount of parameters for operator $word")
    }

    companion object {
        private val entries = HashMap<String, Operator>()

        init {
            for (operator in values())
                entries[operator.word] = operator
        }

        /**
         * Get operator by it's word
         * @return [Operator] or null if there's no such word for operator
         */
        fun getOperator(word: String): Operator? = entries[word]
    }
}
