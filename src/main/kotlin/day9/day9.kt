package day9

import java.io.File
import java.util.*
import kotlin.collections.HashSet

fun isLowest(row: Int, col: Int, lines: List<List<Int>>): Int {
    val points = listOf<Pair<Int, Int>>(
        Pair(row - 1, col),
        Pair(row + 1, col),
        Pair(row, col - 1),
        Pair(row, col + 1)
    ).filter { it.first >= 0 && it.second >= 0 && it.first < lines.size && it.second < lines[0].size }

    val curr = lines[row][col]
    var res = true
    for (point in points) {
        if (curr >= lines[point.first][point.second]) {
            res = false
        }
    }

    if (res) {

        return curr
    } else {
        return -1
    }

}

fun basinSize(row: Int, col: Int, lines: List<List<Int>>): Long {
    val queue: Queue<Pair<Int, Int>> = LinkedList()
    val visited: MutableSet<Pair<Int, Int>> = HashSet()

    queue.add(Pair(row, col))

    var basin = 0
    while (!queue.isEmpty()) {
        val curr = queue.poll()
        if (!visited.contains(curr)) {
            visited.add(curr)

            basin += 1
            val row2 = curr.first
            val col2 = curr.second
            val points = listOf<Pair<Int, Int>>(
                Pair(row2 - 1, col2),
                Pair(row2 + 1, col2),
                Pair(row2, col2 - 1),
                Pair(row2, col2 + 1)
            )
                .filter { it.first >= 0 && it.second >= 0 && it.first < lines.size && it.second < lines[0].size }
                .filter { lines[it.first][it.second] > lines[curr.first][curr.second] && lines[it.first][it.second] < 9 }

            for (point in points) {
                queue.add(point)
            }
        }
    }

    return basin.toLong()

}

fun main(args: Array<String>) {
    val lines = File("files/day9.txt").readLines().map { it.toCharArray().map { Integer.parseInt(it.toString()) } }


    var sum: Long = 1
    var basins: MutableList<Long> = mutableListOf<Long>()
    for (row in 0 until lines.size) {
        for (col in 0 until lines[0].size) {
            val lowest = isLowest(row, col, lines)
            if (lowest >= 0) {

                val basin = basinSize(row, col, lines)
                basins.add(basin)
            }
        }
    }
    basins = basins.sorted().toMutableList()
    val nb = basins.size
    println(basins[nb - 1] * basins[nb - 2] * basins[nb - 3])



    println("Hello World on day9!")
}
