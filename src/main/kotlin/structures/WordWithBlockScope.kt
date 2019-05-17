package structures

/**
 * Represents word that has a body
 * procedures and conditions should inherit it
 */
interface WordWithBlockScope : Word {
    val tree: SyntaxTreeNode
}
