import java.io.File

class SudokuTestCaseParser {
    private val testCaseFiles = listOf(
        File("src/test/resources/testCase1"),
        File("src/test/resources/testCase2"),
        File("src/test/resources/testCase3"),
        File("src/test/resources/testCase4")
    )

    val testCases = testCaseFiles.map { file ->
        val lines = file.readLines().filterNot { it.isEmpty() }
        val sample = lines.take(9).map { line -> line.split(" ").map { it.toInt() } }
        val answer = lines.drop(9).take(9).map { line -> line.split(" ").map { it.toInt() } }

        Pair(sample, answer)
    }
}