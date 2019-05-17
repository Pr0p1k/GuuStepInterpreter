import java.lang.IllegalArgumentException
import java.util.function.Consumer

/**
 * Represents all the operators of the language
 */
enum class Operator(val word: String, val paramsLength: Int,
                    val action: (InterpretationState, List<String>, Int) -> Unit) {
    SUB("sub", 1, { state, params, lineNumber ->
        if (state.procedures.containsKey(params[0]))
            throw RedeclarationException("Procedure \"${params[0]}\" is already" +
                    " declared at line ${state.procedures[params[0]]!!.lineNumber}")

        state.procedures[params[0]] = Procedure(lineNumber, params[0])
        if (params[0] == "main") {
            state.currentLine = lineNumber
        }
    }),
    CALL("call", 1, { state, params, returnNumber ->
        state.callStack.push(Pair(state.procedures[params[0]]
                ?: throw IllegalArgumentException(""), returnNumber)) // TODO
    }),
    SET("set", 2, { state, params, _ ->
        state.variables[params[0]] = params[1].toInt()
    }),
    PRINT("print", 1, { state, params, _ ->
        state.outputHandler.writeString("${params[0]} = ${state.variables[params[0]]}")
    })
}
