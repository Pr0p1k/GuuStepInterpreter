import io.InputHandler
import io.OutputHandler
import structures.Procedure
import structures.StackFrame
import structures.Value
import java.util.*
import kotlin.collections.HashMap

/**
 * Class to hold call stack, I/O handlers and auxiliary data
 */
class InterpretationState {
    lateinit var inputHandler: InputHandler
    lateinit var outputHandler: OutputHandler
    val callStack = Stack<StackFrame>()
    val variables = TreeMap<String, Value<Int>>()
    // if functions with params are about to implement,
    // the key must be function's signature instead of the name
    val procedures = HashMap<String, Procedure>()
    var stepMode: Boolean = true
    var stepIn: Boolean = true
}
