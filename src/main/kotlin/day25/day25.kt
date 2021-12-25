package day25

import java.io.File

typealias Point = Pair<Int, Int>

fun main(args: Array<String>) {
    var currentLines = File("files/day25.txt").readLines().map { it.toCharArray().toMutableList() }.toMutableList()

    var rightState: MutableSet<Point> = mutableSetOf()
    var downState: MutableSet<Point> = mutableSetOf()

    for (y in 0 until currentLines.size) {
        for (x in 0 until currentLines[0].size) {
            if (currentLines[y][x] == '>') {
                rightState.add(Point(x, y))
            } else if (currentLines[y][x] == 'v') {
                downState.add(Point(x, y))
            }
        }
    }

    var count = 0
    while (true) {
        var intersections = 0

        val newRightState = rightState.map { point ->
            ((point.first + 1) % currentLines[0].size).let { newX ->
                val newPoint = Point(newX, point.second)
                if (!(newPoint in rightState || newPoint in downState)) {
                    newPoint
                } else {
                    point
                }
            }
        }
        intersections += newRightState.intersect(rightState).size

        val newDownState = downState.map { point ->
            ((point.second + 1) % currentLines.size).let { newY ->
                val newPoint = Point(point.first, newY)
                if (!(newPoint in newRightState || newPoint in downState)) {
                    newPoint
                } else {
                    point
                }
            }
        }
        intersections += newDownState.intersect(downState).size

        count++
        if (intersections == downState.size + rightState.size) {
            break
        }

        rightState.clear()
        rightState.addAll(newRightState)
        downState.clear()
        downState.addAll(newDownState)
    }

    println(count)
    println("Hello World on day25!")
}
