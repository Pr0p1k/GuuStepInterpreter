import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

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
     * only file name should be passed
     * @param args args with which it's called
     */
    private fun loadState(args: Array<String>) {
        state.variables.clear()
        state.procedures.clear()
        initInterpreter(args)
        val lines = readFile(args.last()).toList()
        hoistDeclarations(lines)
    }

    /**
     * Checks whether interpreter finds main procedure
     */
    @ParameterizedTest
    @CsvSource("src/test/resources/test1.guu, false",
            "src/test/resources/test2.guu, false",
            "src/test/resources/test3.guu, true",
            "src/test/resources/test4.guu, false",
            "src/test/resources/test5.guu, true")
    fun initializeProgram(file: String, shouldThrow: Boolean) {
        loadState(arrayOf(file))
        if (shouldThrow) assertThrows(NoSuchMethodError::class.java, ::initProgram)
        else assertDoesNotThrow(::initProgram)
    }
}
