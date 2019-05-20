package structures

/**
 * A node for syntax tree
 */
class SyntaxTreeNode(val value: Line) {
    val children = mutableListOf<SyntaxTreeNode>()
}
