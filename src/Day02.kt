fun main() {

    val safeValues = 1..3

    fun parse(input: List<String>): List<List<Int>> =
        input.map { line -> line.split(" ").map { it.toInt() } }

    fun List<Int>.removeAt(index: Int): List<Int> = toMutableList().apply { removeAt(index) }

    fun List<Int>.isSafe(isDecreasing: Boolean = true, tolerance: Int = 0): Boolean {

        fun List<Int>.retry(removeAt: Int, newTolerance: Int) = removeAt(removeAt).isSafe(tolerance = newTolerance)

        val unsafeIndex = zipWithNext().mapIndexed { i, (a, b) -> Triple(i, a, b) }.firstOrNull { (i, a, b) ->
            !safeValues.contains((a - b) * if (isDecreasing) 1 else -1)
        }?.first

        val isSafe = if (unsafeIndex != null && isDecreasing) {
            isSafe(isDecreasing = false, tolerance = tolerance)
        } else unsafeIndex == null

        return if (!isSafe && tolerance > 0) {
            val newTolerance = tolerance - 1
            when (unsafeIndex!!) {
                0 -> retry(unsafeIndex, newTolerance) || retry(unsafeIndex + 1, newTolerance)
                else -> retry(unsafeIndex + 1, newTolerance)
            }
        } else isSafe
    }

    fun part1(report: List<List<Int>>): Int = report.count { levels -> levels.isSafe() }
    fun part2(report: List<List<Int>>): Int = report.count { levels -> levels.isSafe(tolerance = 1) }

    val report = parse(readInput("Day02"))
    part1(report).println()
    part2(report).println()
}
