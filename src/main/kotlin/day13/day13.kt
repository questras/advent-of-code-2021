package day13

import java.io.File
import java.lang.Math.abs

data class Coord(
    val x: Int,
    val y: Int
)

data class Fold(
    val value: Int,
    val isX: Boolean
)

fun reflect(coord: Coord, by: Int, isX: Boolean): Coord {
    if (isX) {
        val y = coord.y
        val x = by - abs(coord.x - by)
        return Coord(x, y)
    }
    else {
        val x = coord.x
        val y = by - abs(coord.y - by)
        return Coord(x, y)
    }
}

fun main(args: Array<String>) {
    val lines = File("files/day13.txt").readLines()

    val part1 = lines.filter { it.isNotEmpty() && it[0] != 'f' }.map {
        Coord(
            Integer.parseInt(it.split(",")[0]),
            Integer.parseInt(it.split(",")[1])
        )
    }

    val part2 = lines.filter { it.isNotEmpty() && it[0] == 'f' }.map {
        Fold(
            Integer.parseInt(it.split("=")[1]),
            it.split("=")[0].toCharArray().last() == 'x'
        )
    }

    var maxX = part1.maxByOrNull { it.x }!!.x
    var maxY = part1.maxByOrNull { it.y }!!.y

    var dotted = mutableSetOf<Coord>()
    dotted = part1.toMutableSet()

    for (fold in part2) {
        if (fold.isX) {
            for (c in dotted.filter { it.x > fold.value }) {
                val reflected = reflect(c, fold.value, true)
                dotted.remove(c)
                if (reflected.x >= 0 && reflected.x <= maxX) {
                    dotted.add(reflected)
                }
            }
        }
        else {
            for (c in dotted.filter { it.y > fold.value }) {
                val reflected = reflect(c, fold.value, false)
                dotted.remove(c)
                if (reflected.y >= 0 && reflected.y <= maxY) {
                    dotted.add(reflected)
                }
            }
        }
        println(dotted.size)
    }

    maxX = dotted.maxByOrNull { it.x }!!.x
    maxY = dotted.maxByOrNull { it.y }!!.y
    val minX = dotted.minByOrNull { it.x }!!.x
    val minY = dotted.minByOrNull { it.y }!!.y
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            if (dotted.contains(Coord(x, y))) {
                print('#')
            }
            else {
                print('.')
            }
        }
        println()
    }



    println("Hello World on day13!")
}
