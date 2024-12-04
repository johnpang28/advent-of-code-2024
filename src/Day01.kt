import kotlin.math.abs

fun main() {

    fun parse(input: List<String>): Pair<List<Int>, List<Int>> =
        input.map { line ->
            line.split("   ").map { it.toInt() }.let { it[0] to it[1] }
        }.fold(Pair(emptyList(), emptyList())) { (leftList, rightList), (nextLeft, nextRight) ->
            (leftList + nextLeft) to (rightList + nextRight)
        }

    fun part1(input: List<String>): Int {
        val (leftList, rightList) = parse(input)
        return leftList.sorted().zip(rightList.sorted()).sumOf { abs(it.first - it.second) }
    }

    fun part2(input: List<String>): Int {
        val (leftList, rightList) = parse(input)
        val rightMap = rightList.associateWith { id -> rightList.count { it == id } }
        return leftList.sumOf { it * rightMap.getOrDefault(it, 0) }
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

