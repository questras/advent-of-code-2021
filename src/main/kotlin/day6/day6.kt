package day6

import java.io.File

fun solve(input: List<Int>, numberOfDays: Int): Long {
    var freqVec = mutableListOf<Long>(0, 0, 0, 0, 0, 0, 0, 0, 0)
    for (s in input) {
        freqVec[s]++
    }

    for (i in 0 until numberOfDays) {
        val newVec = mutableListOf<Long>(0, 0, 0, 0, 0, 0, 0, 0, 0)

        newVec[8] = freqVec[0]
        newVec[7] = freqVec[8]
        newVec[6] = freqVec[7] + freqVec[0]
        newVec[5] = freqVec[6]
        newVec[4] = freqVec[5]
        newVec[3] = freqVec[4]
        newVec[2] = freqVec[3]
        newVec[1] = freqVec[2]
        newVec[0] = freqVec[1]

        freqVec = newVec
    }

    return freqVec.sum()
}

fun main(args: Array<String>) {
    val lines = File("files/day6.txt").readLines()[0].split(",").map { it.toInt() }

    println(solve(lines, 80))
    println(solve(lines, 256))
    println("Hello World on day6!")
}
