package day5

import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.abs

fun isHori(line: Line) = line.p1.y == line.p2.y

fun isVerti(line: Line) = line.p1.x == line.p2.x

fun isGoodDiag(line: Line) = abs(line.p1.x - line.p2.x) == abs(line.p1.y - line.p2.y)

fun isLineAccepted(line: Line, acceptDiag: Boolean) =
    isVerti(line) || isHori(line) || (acceptDiag && isGoodDiag(line))

fun getPoints(line: Line, acceptDiag: Boolean): List<Point> {
    val res = mutableListOf<Point>()

    if (isVerti(line)) {
        for (i in min(line.p1.y, line.p2.y) until max(line.p1.y, line.p2.y) + 1) {
            res.add(Point(line.p1.x, i))
        }
    } else if (isHori(line)) {
        for (i in min(line.p2.x, line.p1.x) until max(line.p1.x, line.p2.x) + 1) {
            res.add(Point(i, line.p1.y))
        }
    } else if (acceptDiag) {
        val xs = mutableListOf<Int>()
        val ys = mutableListOf<Int>()

        if (line.p1.x > line.p2.x) {
            for (x in line.p1.x downTo line.p2.x) {
                xs.add(x)
            }
        } else {
            for (x in line.p1.x..line.p2.x) {
                xs.add(x)
            }
        }

        if (line.p1.y > line.p2.y) {
            for (y in line.p1.y downTo line.p2.y) {
                ys.add(y)
            }
        } else {
            for (y in line.p1.y..line.p2.y) {
                ys.add(y)
            }
        }

        for (i in 0 until xs.size) {
            res.add(Point(xs[i], ys[i]))
        }
    }

    return res
}

fun solve(acceptDiag: Boolean): Int {
    val lines = File("files/day5.txt").readLines().map { it.split(" -> ") }
    val plane: MutableList<MutableList<Int>> = mutableListOf()

    for (i in 0..1000) {
        plane.add(mutableListOf())
        for (k in 0..1000) {
            plane[i].add(0)
        }
    }

    for (line in lines) {
        var p1 = line[0].split(",").let { Point(it[0].toInt(), it[1].toInt()) }
        var p2 = line[1].split(",").let { Point(it[0].toInt(), it[1].toInt()) }
        val line = Line(p1, p2)

        if (isLineAccepted(line, acceptDiag)) {
            val points = getPoints(line, acceptDiag)
            for (point in points) {
                plane[point.y][point.x]++
            }
        }
    }

    var sum = 0
    for (i in 0..1000) {
        for (k in 0..1000) {
            if (plane[i][k] > 1) {
                sum++
            }
        }
    }

    return sum
}

fun part1() {
    println(solve(false))
}

fun part2() {
    println(solve(true))
}

fun main(args: Array<String>) {
    println("Hello World on day5!")
    part1()
    part2()
}
