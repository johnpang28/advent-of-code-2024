import Day05.Rule
import Day05.correctOrder
import Day05.isValid
import Day05.midElement
import Day05.parse
import kotlin.io.path.Path
import kotlin.io.path.readText

object Day05 {
    data class Rule(val n1: Int, val n2: Int)

    fun parse(input: String): Pair<List<Rule>, List<List<Int>>> {
        val (ruleInput, pageInput) = input.split("\n\n")
        val rules = ruleInput.lines().map { line ->
            line.split("|").map { it.toInt() }
                .let { (n1, n2) -> Rule(n1, n2) }
        }
        val pages = pageInput.lines().map { line -> line.split(",").map { it.toInt() } }
        return rules to pages
    }

    private fun isValid(rule: Rule, pages: List<Int>): Boolean {
        val i1 = pages.indexOf(rule.n1)
        val i2 = pages.indexOf(rule.n2)
        return if (i1 != -1 && i2 != -1) i1 < i2 else true
    }

    fun isValid(rules: List<Rule>, pages: List<Int>): Boolean =
        rules.all { rule -> isValid(rule, pages) }

    fun List<Int>.midElement(): Int = this[size / 2]

    fun List<Int>.correctOrder(rules: List<Rule>): List<Int>? {
        val initial: List<Int>? = emptyList()
        return fold(initial) { acc, next ->
            acc?.addPageNumber(next, rules)
        }
    }

    private fun List<Int>.addPageNumber(pageNumber: Int, rules: List<Rule>): List<Int>? =
        (0..size).map { i ->
            take(i) + pageNumber + drop(i)
        }.firstOrNull { isValid(rules, it) }
}

fun main() {
    fun part1(rules: List<Rule>, pages: List<List<Int>>): Int {
        val valid = pages.filter { isValid(rules, it) }
        return valid.sumOf { it.midElement() }
    }

    fun part2(rules: List<Rule>, pages: List<List<Int>>): Int {
        val invalid = pages.filter { !isValid(rules, it) }
        val valid = invalid.mapNotNull { it.correctOrder(rules) }
        return valid.sumOf { it.midElement() }
    }

    val (rules, pages) = parse(Path("src/Day05.txt").readText().trim())
    part1(rules, pages).println()
    part2(rules, pages).println()
}
