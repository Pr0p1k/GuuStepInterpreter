import java.util.function.Consumer

/**
 * Represents all the operators of the language
 */
enum class Operator(val word: String, val paramsLength: Int,
                    val action: (InterpretationState, List<String>, Int) -> Unit) {
    SUB("sub", 1, { state, params, lineNumber ->
        if (state.procedures.containsKey(params[1]))
            throw RedeclarationException("Procedure \"${params[1]}\" is already" +
                    " declared at ${state.procedures[params[1]]!!.lineNumber}")

        state.procedures[params[1]] = Procedure(lineNumber, params[1])
        if (params[1] == "main") {
            state.currentLine = lineNumber
        }
    }),
    CALL("call", 1, { state, params, _ ->
        state.callStack.push(state.procedures[params[1]])
    }),
    SET("set", 2, { state, params, _ ->
        state.variables[params[1]] = params[2].toInt()
    }),
    PRINT("print", 1, { state, params, _ ->
        state.outputHandler.writeString("${params[1]} = ${state.variables[params[1]]}")
    })
}
