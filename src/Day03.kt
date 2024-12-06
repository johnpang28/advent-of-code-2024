import Day03.Do
import Day03.Dont
import Day03.Mul
import Day03.Op
import Day03.parseLines

object Day03 {

    sealed interface Op
    data object Do : Op
    data object Dont : Op
    data class Mul(val a: Int, val b: Int) : Op {
        val result = a * b
    }

    private val pattern = """mul\(([0-9]{1,3}),([0-9]{1,3})\)|(do\(\))|(don't\(\))""".toRegex()

    fun parseLines(input: List<String>): List<Op> = input.flatMap { parseLine(it) }

    private fun parseLine(input: String): List<Op> =
        pattern.findAll(input).map {
            val (a, b, doo, dont) = it.destructured
            when {
                doo != "" -> Do
                dont != "" -> Dont
                else -> Mul(a.toInt(), b.toInt())
            }
        }.toList()
}

fun main() {

    fun part1(ops: List<Op>): Int =
        ops.fold(0) { acc, next ->
            when (next) {
                is Mul -> acc + next.result
                else -> acc
            }
        }

    fun part2(ops: List<Op>): Int {
        val initial: Pair<Op, Int> = Do to 0
        val (_, sum) = ops.fold(initial) { (doOrDont, x), op ->
            when (op) {
                Do -> Do to x
                Dont -> Dont to x
                is Mul -> if (doOrDont == Do) doOrDont to x + op.result else doOrDont to x
            }
        }
        return sum
    }

    val ops = parseLines(readInput("Day03"))
    part1(ops).println()
    part2(ops).println()
}
