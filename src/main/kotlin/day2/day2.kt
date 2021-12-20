package day2

import java.io.File

fun main(args: Array<String>) {
    val lines = File("files/day2.txt").readLines().map {it.split(" ")}

    var forw = 0;
    var depth = 0;
    lines.forEach {
        val n = it[1].toInt()
        when (it[0]) {
            "forward" -> forw += n
            "up" -> depth -= n
            "down" -> depth += n
        }
    }
    println(depth * forw)

    forw = 0
    depth = 0
    var aim = 0;

    lines.forEach {
        val n = it[1].toInt()
        when (it[0]) {
            "forward" -> {forw += n; depth += (aim * n)}
            "up" -> aim -= n
            "down" -> aim += n
        }
    }
    println(forw * depth)

    println("Hello World on day2!")
}
