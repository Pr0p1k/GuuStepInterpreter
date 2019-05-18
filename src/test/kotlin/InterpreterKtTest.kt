import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.opentest4j.AssertionFailedError
import java.lang.Exception

internal class InterpreterKtTest {

    companion object {
        @JvmStatic
        @BeforeAll
        fun init() {
            state = InterpretationState()
        }
    }

    /**
     * Make it look like interpreter is started with args
     * @param args args with which it's called
     */
    fun loadState(args: Array<String>) {
        state.variables.clear()
        state.procedures.clear()
        initInterpreter(args)
        val lines = readFile(args.last()).toList()
        hoistDeclarations(lines)
    }

    /**
     * Check whether it finds main procedure
     */
    @Test
    fun initializeProgram() {
        loadState(arrayOf("src/test/resources/test1.guu"))
        assertDoesNotThrow(::initProgram)

        loadState(arrayOf("src/test/resources/test2.guu"))
        assertDoesNotThrow(::initProgram)

        loadState(arrayOf("src/test/resources/test3.guu"))
        assertThrows(NoSuchMethodError::class.java, ::initProgram)

        loadState(arrayOf("src/test/resources/test4.guu"))
        assertDoesNotThrow(::initProgram)

        loadState(arrayOf("src/test/resources/test5.guu"))
        assertThrows(NoSuchMethodError::class.java, ::initProgram)
    }
}
