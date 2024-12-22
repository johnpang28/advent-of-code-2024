import Day08.Coord
import Day08.antinodes
import Day08.invert
import Day08.parse

object Day08 {

    data class Coord(val x: Int, val y: Int)

    fun parse(input: List<String>): Map<Coord, Char> =
        input.flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, frequency ->
                Coord(x, y) to frequency
            }
        }.toMap()

    fun Map<Coord, Char>.invert(): Map<Char, List<Coord>> =
        entries.groupBy { (_, f) -> f }.mapValues { (_, cfs) -> cfs.map { (c, _) -> c } }

    fun antinodes(frequencyMap: Map<Char, List<Coord>>, boundariesFn: (Sequence<Coord>) -> (Sequence<Coord>)): Set<Coord> =
        frequencyMap.flatMap { (frequency, coords) ->
            if (frequency == '.') emptyList()
            else antinodes(permutations(coords), boundariesFn)
        }.toSet()

    private fun permutations(coords: List<Coord>): List<Pair<Coord, Coord>> {
        // all permutations
        val all = coords.flatMap { c1 ->
            coords.filter { it != c1 }.map { c2 -> setOf(c1, c2) }
        }
        // dedupe i.e (a,b) and (b,a) are the same
        return all.toSet().map {
            it.toList().let { (c1, c2) -> c1 to c2 }
        }
    }

    private fun antinodes(coordPairs: List<Pair<Coord, Coord>>, boundariesFn: (Sequence<Coord>) -> (Sequence<Coord>)): List<Coord> =
        coordPairs.flatMap { (c1, c2) ->
            val dx = c1.x - c2.x
            val dy = c1.y - c2.y
            val s1 = boundariesFn(generateSequence(c1) { Coord(it.x + dx, it.y + dy) })
            val s2 = boundariesFn(generateSequence(c2) { Coord(it.x - dx, it.y - dy) })
            s1 + s2
        }
}

fun main() {
    fun part1(frequencyToCoordMap: Map<Char, List<Coord>>, allCoords: Set<Coord>): Int =
        antinodes(frequencyToCoordMap) { sequence ->
            sequence.drop(1).take(1).filter { allCoords.contains(it) }
        }.count()

    fun part2(frequencyToCoordMap: Map<Char, List<Coord>>, allCoords: Set<Coord>): Int =
        antinodes(frequencyToCoordMap) { sequence ->
            sequence.takeWhile { allCoords.contains(it) }
        }.count()

    val coordToFrequencyMap = parse(readInput("Day08"))
    val frequencyToCoordMap = coordToFrequencyMap.invert()
    val allCoords = coordToFrequencyMap.keys
    part1(frequencyToCoordMap, allCoords).println()
    part2(frequencyToCoordMap, allCoords).println()
}
