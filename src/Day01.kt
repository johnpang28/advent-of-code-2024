import kotlin.math.abs

fun main() {

    fun parse(input: List<String>): Pair<List<Int>, List<Int>> =
        input.map { line ->
            line.split("   ").map { it.toInt() }.let { (l, r) -> l to r }
        }.fold(Pair(emptyList(), emptyList())) { (leftList, rightList), (nextLeft, nextRight) ->
            (leftList + nextLeft) to (rightList + nextRight)
        }

    fun part1(leftList: List<Int>, rightList: List<Int>): Int =
        leftList.sorted().zip(rightList.sorted()).sumOf { (a, b) -> abs(a - b) }

    fun part2(leftList: List<Int>, rightList: List<Int>): Int {
        val rightMap = rightList.associateWith { id -> rightList.count { it == id } }
        return leftList.sumOf { it * rightMap.getOrDefault(it, 0) }
    }

    val (leftList, rightList) = parse(readInput("Day01"))
    part1(leftList, rightList).println()
    part2(leftList, rightList).println()
}

