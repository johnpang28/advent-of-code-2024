import Day10.Coord
import Day10.Direction.*
import Day10.parse
import Day10.starts
import Day10.endCoords

object Day10 {

    data class Coord(val x: Int, val y: Int) {
        fun move(direction: Direction): Coord = when (direction) {
            Up -> copy(y = y - 1)
            Down -> copy(y = y + 1)
            Left -> copy(x = x - 1)
            Right -> copy(x = x + 1)
        }
    }

    enum class Direction { Up, Down, Left, Right }

    fun parse(input: List<String>): Map<Coord, Int> =
        input.flatMapIndexed { y, line ->
            line.mapIndexed { x, c ->
                Coord(x, y) to c.digitToInt()
            }
        }.toMap()

    fun Map<Coord, Int>.starts(): List<Coord> =
        filter { (_, v) -> v == 0 }.map { (k, _) -> k }

    fun Map<Coord, Int>.endCoords(start: Coord): List<Coord> {
        fun loop(c: Coord, h: Int): List<Coord> {
            return if (h == 9) listOf(c)
            else {
                Direction.entries.flatMap { d ->
                    val nextH = h + 1
                    val nextC = c.move(d)
                    if (this[nextC] == nextH) loop(nextC, nextH)
                    else emptyList()
                }
            }
        }
        return loop(start, this.getValue(start))
    }
}

fun main() {
    fun part1(heightMap: Map<Coord, Int>, starts: List<Coord>): Int =
        starts.sumOf { heightMap.endCoords(it).toSet().count() }

    fun part2(heightMap: Map<Coord, Int>, starts: List<Coord>): Int =
        starts.sumOf { heightMap.endCoords(it).count() }

    val heightMap = parse(readInput("Day10"))
    val starts = heightMap.starts()
    part1(heightMap, starts).println()
    part2(heightMap, starts).println()
}
