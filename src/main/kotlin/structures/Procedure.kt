package structures

/**
 * Class represents procedure with it's declaration line number, name and tree of operations inside.
 * If functions are about to implement, they should be derived from this class
 */
data class Procedure(val lineNumber: Int, val name: String) : WordWithBlockScope {
    override val tree = SyntaxTreeNode(Line(Operator.SUB, listOf(Param(name))))
}
