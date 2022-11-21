import kotlin.test.Test

class SudokuSolverTest {
    val testCases = SudokuTestCaseParser().testCases

    private val sudokuSolver = SudokuSolver()

    @Test
    fun testTestCases() {
        for ((index, testCase) in testCases.withIndex()) {
            println("----------------------- TEST ${index + 1} ------------------------------")
            val (sample, answer) = testCase
            println("-----------SAMPLE-----------")
            printMatrix(sample)
            val result = sudokuSolver.solve(sample as MutableList<MutableList<Int>>)
            try {
                checkResult(result, answer)
            } catch (e: Exception) {
                println("-----------EXPECTED-----------")
                printMatrix(answer)
                println("-----------ACTUAL-----------")
                result?.let { printMatrix(it) } ?: println("null")
                throw e
            }
            println("-----------RESULT-----------")
            printMatrix(answer)
            println()
        }
    }

    private fun checkResult(result: List<List<Int>>?, answer: List<List<Int>>?) {
        if (answer == null) {
            check(result == null)
            return
        }
        requireNotNull(result)
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                check(result[i][j] == answer[i][j])
            }
        }
    }

    private fun printMatrix(matrix: List<List<Int>>) {
        for (row in matrix) {
            for (cell in row) {
                print(String.format("%3d", cell))
            }
            println()
        }
    }
}