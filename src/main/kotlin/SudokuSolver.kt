class SudokuSolver {
    fun solve(matrix: MutableList<MutableList<Int>>): MutableList<MutableList<Int>>? {
        var candidates = collectCandidates(matrix) ?: return null

        while (candidates.any { it.any { it.isNotEmpty() } }) {
            if (!processSingleDigitCase(candidates, matrix) && !processCandidatesComplexCase(candidates, matrix)) {
                return tryToSolve(matrix, candidates)
            }
            candidates = collectCandidates(matrix) ?: return null
        }

        return matrix
    }

    private fun tryToSolve(
        matrix: MutableList<MutableList<Int>>,
        candidates: List<List<Set<Int>>>
    ): MutableList<MutableList<Int>>? {
        var currentMatrix = copy(matrix)

        for (i in 0 until 9) {
            for (j in 0 until 9) {
                val value = currentMatrix[i][j]
                if (value == -1) {
                    for (candidate in candidates[i][j]) {
                        currentMatrix[i][j] = candidate
                        val result = solve(currentMatrix)
                        if (result == null) {
                            currentMatrix = copy(matrix)
                            continue
                        } else {
                            return result
                        }
                    }
                }
            }
        }
        return null
    }

    private fun copy(matrix: MutableList<MutableList<Int>>): MutableList<MutableList<Int>> {
        val currentMatrix = mutableListOf<MutableList<Int>>()
        for (i in 0 until 9) {
            val list = mutableListOf<Int>()
            for (j in 0 until 9) {
                list.add(matrix[i][j])
            }
            currentMatrix.add(list)
        }
        return currentMatrix
    }

    private fun processCandidatesComplexCase(
        candidates: List<List<Set<Int>>>,
        result: MutableList<MutableList<Int>>
    ): Boolean {
        var hasChanged = false
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                val candidatesInRow = candidates[i]
                    .withIndex()
                    .filterNot { it.index == j }
                    .flatMap { it.value }
                    .toSet()

                val candidatesInColumn = candidates
                    .map { it[j] }
                    .withIndex()
                    .filterNot { it.index == i }
                    .flatMap { it.value }
                    .toSet()

                val candidatesInBlock = candidates
                    .withIndex()
                    .filter { row -> i / 3 * 3 <= row.index && row.index <= i / 3 * 3 + 2 }
                    .flatMap { (_, row) -> listOf(row[j / 3 * 3], row[j / 3 * 3 + 1], row[j / 3 * 3 + 2]) }
                    .withIndex()
                    .filterNot { it.index == (i % 3) * 3 + j % 3 }
                    .flatMap { it.value }
                    .toSet()

                for (candidate in candidates[i][j]) {
                    if (candidate !in candidatesInRow
                        || candidate !in candidatesInColumn
                        || candidate !in candidatesInBlock
                    ) {
                        result[i][j] = candidate
                        hasChanged = true
                    }
                }
            }
        }
        return hasChanged
    }

    private fun processSingleDigitCase(
        candidates: List<List<Set<Int>>>,
        result: MutableList<MutableList<Int>>
    ): Boolean {
        var hasChasnged = false
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                if (candidates[i][j].size == 1) {
                    result[i][j] = candidates[i][j].first()
                    hasChasnged = true
                }
            }
        }
        return hasChasnged
    }

    private fun collectCandidates(matrix: List<List<Int>>): List<List<Set<Int>>>? {
        val candidates = mutableListOf<MutableList<Set<Int>>>()
        for (i in 0 until 9) {
            val row = mutableListOf<Set<Int>>()
            for (j in 0 until 9) {
                row.add(setOf())
            }
            candidates.add(row)
        }
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                try {
                    collectCandidatesByCell(i, j, candidates, matrix)
                } catch (_: IllegalStateException) {
                    return null
                }
            }
        }
        return candidates
    }

    private fun collectCandidatesByCell(
        i: Int,
        j: Int,
        candidates: MutableList<MutableList<Set<Int>>>,
        matrix: List<List<Int>>
    ) {
        val value = matrix[i][j]

        val row = matrix[i].filterNot { it == -1 }
        val column = matrix.map { it[j] }.filterNot { it == -1 }
        val block = matrix.withIndex().filter { row ->
            i / 3 * 3 <= row.index && row.index <= i / 3 * 3 + 2
        }.flatMap { (_, row) ->
            listOf(row[j / 3 * 3], row[j / 3 * 3 + 1], row[j / 3 * 3 + 2])
        }.filterNot { it == -1 }

        val candidatesInRow = row.toSet()
        val candidatesInColumn = column.toSet()
        val candidatesInBlock = block.toSet()

        check(row.size == candidatesInRow.size)
        check(column.size == candidatesInColumn.size)
        check(block.size == candidatesInBlock.size)

        if (value == -1) {
            candidates[i][j] = setOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .minus(candidatesInRow)
                .minus(candidatesInColumn)
                .minus(candidatesInBlock)
        }
    }
}