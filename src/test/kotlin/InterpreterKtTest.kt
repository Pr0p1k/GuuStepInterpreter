import io.QueueOutputHandler
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.*
import java.util.concurrent.ArrayBlockingQueue

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
    private fun loadState(args: Array<String>): List<String> {
        state.variables.clear()
        state.procedures.clear()
        initInterpreter(args)
        val lines = readFile(args.last()).toList()
        hoistDeclarations(lines)
        return lines
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

    /**
     * Check the program's output
     */
    @ParameterizedTest
    @CsvSource("src/test/resources/test2.guu, k = 9/k = 13",
            "src/test/resources/test4.guu, k = /k = -4")
    fun runProgram(fileName: String, output: String) {
        val lines = loadState(arrayOf("-r", fileName))
        loadProcedureBodies(lines)
        val debugQueue = ArrayBlockingQueue<String>(10) // enough for these tests
        val outputQueue = ArrayBlockingQueue<String>(10)
        state.outputHandler = QueueOutputHandler(debugQueue, outputQueue)
        initProgram()
        startInterpreter()
        val outputLines = output.split("/")
        outputLines.forEach {
            assertEquals(outputQueue.take(), it)
        }
    }
}
