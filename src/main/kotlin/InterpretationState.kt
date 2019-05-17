import sun.reflect.generics.tree.Tree
import java.util.*
import kotlin.collections.HashMap

/**
 * Class to hold call stack, syntax tree and auxiliary
 */
class InterpretationState {
    lateinit var inputHandler: InputHandler
    lateinit var outputHandler: OutputHandler
    val callStack = Stack<Pair<Procedure, Int>>()
    val variables = TreeMap<String, Int>()
    // if functions with params are about to implement,
    // then naming constraints should be added and
    // the key must be function's signature instead of the name
    val procedures = HashMap<String, Procedure>()
    var currentLine = -1

    constructor(/*here should be st*/) {
    }
}
