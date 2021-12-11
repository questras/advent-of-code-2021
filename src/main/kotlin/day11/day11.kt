package day11

import java.io.File
import java.util.*
import kotlin.collections.HashSet

fun getAdjacent(row: Int, col: Int, lines: List<List<Int>>): List<Pair<Int, Int>> {
    val points = listOf<Pair<Int, Int>>(
        Pair(row - 1, col),
        Pair(row + 1, col),
        Pair(row, col - 1),
        Pair(row, col + 1),
        Pair(row - 1, col - 1),
        Pair(row + 1, col + 1),
        Pair(row - 1, col + 1),
        Pair(row + 1, col - 1)
    ).filter { it.first >= 0 && it.second >= 0 && it.first < lines.size && it.second < lines[0].size }

    return points
}

fun main(args: Array<String>) {
    var lines = File("files/day11.txt").readLines().map { line -> line.toCharArray().map { Integer.parseInt(it.toString()) }.toMutableList() }.toMutableList()

    val steps = 100
    var sum = 0

    val newLines = lines
    var step = 0

    while (true) {
        step += 1
        val flashed = HashSet<Pair<Int, Int>>()
        val flashQueue = LinkedList<Pair<Int, Int>>()

        // First, the energy level of each octopus increases by 1.
        for (row in 0 until lines.size) {
            for (col in 0 until lines[0].size) {
                newLines[row][col] += 1
                if (newLines[row][col] > 9) {
                    newLines[row][col] = 0
                    flashQueue.add(Pair(row, col))
                }
            }
        }

        while (!flashQueue.isEmpty()) {
            val currentPair = flashQueue.poll()

            if (!flashed.contains(currentPair)) {
                flashed.add(currentPair).also { sum += 1 }
                newLines[currentPair.first][currentPair.second] = 0
                val neighbours = getAdjacent(currentPair.first, currentPair.second, lines)
                for (p in neighbours) {
                    if (!flashed.contains(p)) {
                        newLines[p.first][p.second] += 1
                        if (newLines[p.first][p.second] > 9) {
                            flashQueue.add(p)
                        }
                    }
                }

            }
        }

        var isAll = true
        for (row in 0 until lines.size) {
            for (col in 0 until lines[0].size) {
                if (newLines[row][col] != 0) {
                    isAll = false
                    break
                }
            }
        }

        if (isAll) {
            for (line in newLines) {
                println(line)
            }
            println(step-1)
            return
        }

    }

    for (line in newLines) {
        println(line)
    }
    println(sum)
    println("Hello World on day11!")
}
