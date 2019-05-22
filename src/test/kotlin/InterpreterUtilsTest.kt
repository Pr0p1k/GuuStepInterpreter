import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import structures.Line
import structures.Operator
import structures.Param

internal class InterpreterUtilsTest {

    @ParameterizedTest
    @CsvSource("src/test/resources/test1.guu, 21",
            "src/test/resources/test2.guu, 16",
            "src/test/resources/test3.guu, 0",
            "src/test/resources/test4.guu, 6",
            "src/test/resources/test5.guu, 7")
    fun readFile(file: String, linesNumber: Int) {
        assertEquals(readFile(file).toList().size, linesNumber)
    }

    /**
     * Checks line normalization
     */
    @ParameterizedTest
    @CsvSource("      print      heh , print heh",
            "\tsub \t\t\tkek\t, sub kek")
    fun normalizeLine(badLine: String, goodLine: String) {
        val words = getWords(badLine)
        val rebuiltLine = normalizeLine(
                Line(Operator.getOperator(words[0])!!, words.map { Param(it) }.subList(1, words.size)))
        assertEquals(rebuiltLine, goodLine)
    }
}
