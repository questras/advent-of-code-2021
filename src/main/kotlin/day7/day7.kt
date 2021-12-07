package day7

import java.io.File
import kotlin.math.abs
import java.lang.Math.min

fun solve(costFn: (Int, Int) -> Int): Int {
    val crabs = File("files/day7.txt").readLines()[0].split(",").map { it.toInt() }
    val maxPosition = crabs.maxOf { it }

    var mini = Int.MAX_VALUE
    for (pos in 0..maxPosition) {
        var sum = 0
        for (crab in crabs) {
            sum += costFn(pos, crab) // ((1 + abs(i - l)) * abs(i - l) / 2)
        }

        mini = min(mini, sum)
    }

    return mini
}

fun main(args: Array<String>) {

    println(solve { pos, crab -> abs(pos - crab) })
    println(solve { pos, crab -> (1 + abs(pos - crab)) * abs(pos - crab) / 2})

    println("Hello World on day7!")
}
