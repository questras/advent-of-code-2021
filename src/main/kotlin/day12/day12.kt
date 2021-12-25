package day12

import java.io.File
import java.util.*

fun isLowercase(dest: String): Boolean = dest == dest.lowercase(Locale.getDefault())

fun <T> clone(original: Set<T>): MutableSet<T> = HashSet(original)

fun solve(paths: List<Path>, visited: MutableSet<String>, current: String, usedLowercaseTwice: Boolean): Int {
    if (current == "end") {
        return 1
    }
    if (isLowercase(current)) {
        visited.add(current)
    }

    val allStarts: List<String>
    val allEnds: List<String>

    return if (usedLowercaseTwice) {
        allStarts = paths.filter { !visited.contains(it.start) }.filter { it.end == current }.map { it.start }
        allEnds = paths.filter { !visited.contains(it.end) }.filter { it.start == current }.map { it.end }

        (allEnds + allStarts).sumOf { solve(paths, clone(visited), it, true) }
    } else {
        allStarts = paths.filter { it.end == current }.map { it.start }.filter { it != "start" }
        allEnds = paths.filter { it.start == current }.map { it.end }.filter { it != "start" }

        (allEnds + allStarts).sumOf { solve(paths, clone(visited), it, visited.contains(it)) }
    }

}

data class Path(
    val start: String,
    val end: String
)

fun main(args: Array<String>) {
    val lines = File("files/day12.txt").readLines()
    val paths = lines.map { Path(it.split("-")[0], it.split("-")[1]) }

    println(solve(paths, mutableSetOf(), "start", true))
    println(solve(paths, mutableSetOf(), "start", false))

    println("Hello World on day12!")
}
