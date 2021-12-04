package day4

import java.io.File

fun part1(boards: MutableList<Board>, order: List<Int>) {
    for (i in order) {
        for (board in boards) {
            board.setNumber(i)

            if (board.isAnyRowDone() || board.isAnyColumnDone()) {
                println(board.getSumOfRest() * i)
                return
            }
        }
    }
}

fun part2(boards: MutableList<Board>, order: List<Int>) {
    var wonBoards = 0
    val doneBoards = mutableListOf<Boolean>()

    for (i in 0 until boards.size) {
        doneBoards.add(false)
    }

    for (i in order) {
        for (b in 0 until boards.size) {
            if (!doneBoards[b]) {
                boards[b].setNumber(i)

                if (boards[b].isAnyRowDone() || boards[b].isAnyColumnDone()) {
                    wonBoards++
                    doneBoards[b] = true
                    if (wonBoards == boards.size) {
                        println(boards[b].getSumOfRest() * i)
                    }
                }
            }
        }
    }
}

fun main(args: Array<String>) {
    val allLines = File("files/day4.txt").readLines()
    val lines = mutableListOf<String>()
    for (i in 1 until allLines.size) {
        lines.add(allLines[i])
    }
    val order = allLines[0].split(",").map { it.toInt() }

    val boards: MutableList<Board> = mutableListOf()
    val nBoards = (lines.size - 1) / 6

    for (i in 0 until nBoards) {
        val board: MutableList<MutableList<Int>> = mutableListOf()

        for (k in 1 until 6) {
            val line = lines[(i * 6) + k].split("\\s+".toRegex()).filter { it != "" }.map { it.toInt() }.toMutableList()
            board.add(line)
        }
        boards.add(Board(board))
    }

    part1(boards, order)
    part2(boards, order)

    println("Hello World on day4!")
}
