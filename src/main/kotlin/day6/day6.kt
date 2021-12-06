package day6

import java.io.File

// f(n) = n/6

fun g(n: Int): Int {
    var sum = 1
    var newN = n
    if (n < 8) {
        return sum
    }

    sum += g(n-8)
    newN -= 8

    while (newN > 5) {
        sum += g(newN)
        newN -= 6
    }

    return sum
}

fun f(n: Int, first: Int): Int {
    var sum = 1
    var newN = n
    if (n < first) {
        return sum
    }

    sum += g(n-first)
    newN -= first

    while (newN > 5) {
        sum += g(newN)
        newN -= 6
    }

    return sum
}

fun main(args: Array<String>) {
    val lines = File("files/day6.txt").readLines()[0].split(",").map { it.toInt() }

    val states = lines.toMutableList()
    val numberOfDays = 256

    var freqVec = mutableListOf<Long>(0, 0, 0, 0, 0, 0, 0, 0, 0)
    for (s in states) {
        freqVec[s]++
    }
    println(freqVec)

    for (i in 0 until numberOfDays) {
        var newVec = mutableListOf<Long>(0, 0, 0, 0, 0, 0, 0, 0, 0)

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

    println(freqVec.sum())



    println("Hello World on day6!")
}
