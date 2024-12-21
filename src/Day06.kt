import Day06.Coord
import Day06.Direction.*
import Day06.Guard
import Day06.parse
import Day06.path
import Day06.willLoop

object Day06 {

    data class Coord(val x: Int, val y: Int) {
        fun move(direction: Direction): Coord = when (direction) {
            Up -> Coord(x, y - 1)
            Down -> Coord(x, y + 1)
            Left -> Coord(x - 1, y)
            Right -> Coord(x + 1, y)
        }
    }

    data class Guard(val position: Coord, val direction: Direction) {
        fun nextPosition(): Coord = position.move(direction)
        fun turn(): Guard = Guard(position, direction.turn())
        fun forward(): Guard = Guard(nextPosition(), direction)
    }

    enum class Direction {
        Up, Down, Left, Right;

        fun turn(): Direction = when (this) {
            Up -> Right
            Down -> Left
            Left -> Up
            Right -> Down
        }
    }

    fun parse(input: List<String>): Pair<Guard, Map<Coord, Boolean>> {
        val obstructionMap = mutableMapOf<Coord, Boolean>()
        var guard: Guard? = null
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when (c) {
                    '^' -> {
                        guard = Guard(Coord(x, y), Up)
                        obstructionMap[Coord(x, y)] = false
                    }

                    '#' -> obstructionMap[Coord(x, y)] = true
                    else -> obstructionMap[Coord(x, y)] = false
                }
            }
        }
        return guard!! to obstructionMap
    }

    private fun move(guard: Guard, obstructionMap: Map<Coord, Boolean>): Guard? =
        when (obstructionMap[guard.nextPosition()]) {
            true -> guard.turn()
            false -> guard.forward()
            null -> null
        }

    fun path(guard: Guard, obstructionMap: Map<Coord, Boolean>): Set<Coord> {
        fun loop(g: Guard?, visited: Set<Coord>): Set<Coord> {
            return if (g == null) visited
            else {
                val next = move(g, obstructionMap)
                loop(next, visited + g.position)
            }
        }
        return loop(guard, emptySet())
    }

    fun willLoop(guard: Guard, obstructionMap: Map<Coord, Boolean>): Boolean {
        val history = mutableSetOf<Guard>() // functional approach is too slow
        fun loop(g: Guard?): Boolean {
            return if (g == null) false
            else {
                val next = move(g, obstructionMap)
                if (history.contains(next)) true
                else {
                    history.add(g)
                    loop(next)
                }
            }
        }
        return loop(guard)
    }
}

fun main() {
    fun part1(path: Set<Coord>): Int = path.count()

    fun part2(guard: Guard, obstructionMap: Map<Coord, Boolean>, path: Set<Coord>): Int =
        path.count { willLoop(guard, obstructionMap + (it to true)) }

    val (guard, obstructionMap) = parse(readInput("Day06"))
    val path = path(guard, obstructionMap)
    part1(path).println()
    part2(guard, obstructionMap, path).println()
}
