import Day09.Block
import Day09.checksum
import Day09.compact1
import Day09.compact2
import Day09.parse
import kotlin.io.path.Path
import kotlin.io.path.readText

object Day09 {
    sealed interface Block
    data class Data(val id: Int) : Block
    data object FreeSpace : Block

    fun parse(input: String): List<Block> =
        input.flatMapIndexed { i, c ->
            (0 until c.digitToInt()).map {
                if (i % 2 == 0) Data((i / 2)) else FreeSpace
            }
        }

    fun List<Block>.compact1(): List<Block> {
        val mutableBlocks = toMutableList()
        run breaking@{
            indices.forEach { i ->
                if (mutableBlocks[i] is FreeSpace) {
                    val lastDataIndex = mutableBlocks.indexOfLast { it is Data }
                    if (lastDataIndex <= i) return@breaking
                    mutableBlocks.swap(i, lastDataIndex)
                }
            }
        }
        return mutableBlocks
    }

    fun List<Block>.compact2(): List<Block> {
        val mutableBlocks = toMutableList()
        val maxId = (this[indexOfLast { it is Data }] as Data).id
        (maxId downTo 0).forEach { id ->
            val fileIndexes = mutableBlocks.fileIndexes(id)
            val freeSpaceIndexes = mutableBlocks.freeSpaceIndexes(fileIndexes.first(), fileIndexes.size)
            if (freeSpaceIndexes != null) {
                fileIndexes.forEachIndexed { i, fileIndex ->
                    mutableBlocks.swap(fileIndex, freeSpaceIndexes[i])
                }
            }
        }
        return mutableBlocks
    }

    private fun MutableList<Block>.swap(i1: Int, i2: Int) {
        val b1 = this[i1]
        val b2 = this[i2]
        this[i1] = b2
        this[i2] = b1
    }

    private fun List<Block>.fileIndexes(id: Int): List<Int> =
        indices.filter {
            when (val block = this[it]) {
                is Data -> block.id == id
                FreeSpace -> false
            }
        }

    private fun List<Block>.freeSpaceIndexes(before: Int, size: Int): List<Int>? {
        val freeSpaceIndexes = mutableListOf<Int>()
        (0 until before).forEach { i ->
            when (this[i]) {
                is Data -> if (freeSpaceIndexes.size >= size) return freeSpaceIndexes else freeSpaceIndexes.clear()
                FreeSpace -> freeSpaceIndexes.add(i)
            }
        }
        return if (freeSpaceIndexes.size >= size) freeSpaceIndexes else null
    }

    fun List<Block>.checksum(): Long =
        indices.sumOf {
            when (val block = this[it]) {
                is Data -> block.id.toLong() * it
                FreeSpace -> 0
            }
        }
}

fun main() {

    fun part1(blocks: List<Block>): Long = blocks.compact1().checksum()
    fun part2(blocks: List<Block>): Long = blocks.compact2().checksum()

    val blocks = parse(Path("src/Day09.txt").readText().trim())
    part1(blocks).println()
    part2(blocks).println()
}
