package day1

import java.io.File

fun main(args: Array<String>) {
    val lines = File("files/day1.txt").readLines().map { it.toInt() }

    var counter1 = 0;
    for (i in 1 until lines.size) {
        if (lines[i] > lines[i-1]) counter1++
    }
    println(counter1)

    var counter2 = 0;
    for (i in 3..lines.size - 1) {
        val sum1 = lines[i - 2] + lines[i - 1] + lines[i];
        val sum2 = lines[i-3] + lines[i-2] + lines[i-1];
        if (sum1 > sum2) {
            counter2++
        }
    }
    println(counter2)
}
