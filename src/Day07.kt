import Day07.Add
import Day07.Concat
import Day07.Multiply
import Day07.parse
import Day07.validValues

object Day07 {

    sealed interface Operator {
        fun apply(a: Long, b: Long): Long
    }

    data object Add : Operator {
        override fun apply(a: Long, b: Long): Long = a + b
    }

    data object Multiply : Operator {
        override fun apply(a: Long, b: Long): Long = a * b
    }

    data object Concat : Operator {
        override fun apply(a: Long, b: Long): Long = (a.toString() + b.toString()).toLong()
    }

    fun parse(inputs: List<String>): List<Pair<Long, List<Long>>> =
        inputs.map { line ->
            val (left, right) = line.split(": ")
            left.toLong() to right.split(" ").map { it.toLong() }
        }

    // Could be optimized by memoizing the permutations
    private fun opsPermutations(opCount: Int, operators: List<Operator>): List<List<Operator>> {
        fun loop(n: Int, acc: List<List<Operator>>): List<List<Operator>> {
            return if (n == 0) acc
            else loop(
                n - 1,
                operators.flatMap { op ->
                    if (acc.isEmpty()) listOf(listOf(op))
                    else acc.map { it + op }
                }
            )
        }
        return loop(opCount, emptyList())
    }

    private fun eval(args: List<Long>, ops: List<Operator>): Long =
        args.drop(1).foldIndexed(args.first()) { i, acc, next ->
            ops[i].apply(acc, next)
        }

    private fun isValid(value: Long, args: List<Long>, operators: List<Operator>): Boolean =
        opsPermutations(args.size - 1, operators).any { ops ->
            eval(args, ops) == value
        }

    fun validValues(input: List<Pair<Long, List<Long>>>, operators: List<Operator>): List<Long> =
        input.mapNotNull { (value, args) ->
            if (isValid(value, args, operators)) value else null
        }
}

fun main() {

    fun part1(input: List<Pair<Long, List<Long>>>): Long =
        validValues(input, listOf(Add, Multiply)).sum()

    fun part2(input: List<Pair<Long, List<Long>>>): Long =
        validValues(input, listOf(Add, Multiply, Concat)).sum()

    val input = parse(readInput("Day07"))
    part1(input).println()
    part2(input).println()
}
