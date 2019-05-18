import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertAll

internal class InterpreterUtilsTest {

    @Test
    fun readFile() {
        assertAll("Read files",
                { assertEquals(readFile("src/test/resources/test1.guu").toList().size, 21) },
                { assertEquals(readFile("src/test/resources/test2.guu").toList().size, 16) },
                { assertEquals(readFile("src/test/resources/test3.guu").toList().size, 0) },
                { assertEquals(readFile("src/test/resources/test4.guu").toList().size, 4) },
                { assertEquals(readFile("src/test/resources/test5.guu").toList().size, 7) }
        )
    }

}
