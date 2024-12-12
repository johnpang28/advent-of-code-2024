import Day04.Direction.NE
import Day04.Direction.NW
import Day04.Direction.SE
import Day04.Direction.SW
import Day04.WordSearch
import Day04.countWord
import Day04.countXmas
import Day04.parse

object Day04 {
    data class Coord(val x: Int, val y: Int) {
        fun move(direction: Direction): Coord = Coord(x + direction.dx, y + direction.dy)
    }

    data class WordSearch(val charMap: Map<Coord, Char>, val xMax: Int, val yMax: Int)

    enum class Direction(val dx: Int, val dy: Int) {
        N(0, -1),
        NE(1, -1),
        E(1, 0),
        SE(1, 1),
        S(0, 1),
        SW(-1, 1),
        W(-1, 0),
        NW(-1, -1)
    }

    fun parse(input: List<String>): WordSearch =
        WordSearch(
            charMap = input.indices.flatMap { y ->
                input[y].indices.map { x ->
                    Coord(x, y) to input[y][x]
                }
            }.toMap(),
            xMax = input[0].length - 1,
            yMax = input.size - 1
        )

    fun WordSearch.countWord(word: String): Int =
        (0..yMax).flatMap { y ->
            (0..xMax).flatMap { x ->
                Direction.entries.map { direction ->
                    match(word, Coord(x, y), direction)
                }
            }
        }.count { it }

    fun WordSearch.countXmas(): Int =
        (1..<yMax).flatMap { y ->
            (1..<xMax).map { x ->
                val middle = Coord(x, y)
                if (charMap[middle] == 'A')
                    listOf(
                        middle.move(NW) to middle.move(SE),
                        middle.move(NE) to middle.move(SW),
                        middle.move(SE) to middle.move(NW),
                        middle.move(SW) to middle.move(NE),
                    ).count { (start, end) -> charMap[start] == 'M' && charMap[end] == 'S' } == 2
                else false
            }
        }.count { it }

    private fun WordSearch.match(word: String, start: Coord, direction: Direction): Boolean =
        if (word.isEmpty()) true
        else charMap[start]?.let { char ->
            if (char == word[0]) match(word.drop(1), start.move(direction), direction)
            else false
        } ?: false
}

fun main() {
    fun part1(wordSearch: WordSearch): Int = wordSearch.countWord("XMAS")
    fun part2(wordSearch: WordSearch): Int = wordSearch.countXmas()

    val wordSearch = parse(readInput("Day04"))
    part1(wordSearch).println()
    part2(wordSearch).println()
}
