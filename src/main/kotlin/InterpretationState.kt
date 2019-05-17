import io.InputHandler
import io.OutputHandler
import structures.Procedure
import java.util.*
import kotlin.collections.HashMap

/**
 * Class to hold call stack, I/O handlers and auxiliary data
 */
class InterpretationState {
    lateinit var inputHandler: InputHandler
    lateinit var outputHandler: OutputHandler
    val callStack = Stack<Pair<Procedure, Int>>()
    val variables = TreeMap<String, Int>()
    // if functions with params are about to implement,
    // the key must be function's signature instead of the name
    val procedures = HashMap<String, Procedure>()
    var currentLine = -1
}
