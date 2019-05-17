/**
 * A node for syntax tree
 */
class SyntaxTreeNode(val value: String) {
    val children = mutableListOf<SyntaxTreeNode>()
}
