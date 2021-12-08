package day8

import java.io.File

fun f(s: String, chars: Map<String, Int>): Int {
    if (s.length == 2) return 1
    else if (s.length == 3) return 7
    else if (s.length == 4) return 4
    else if (s.length == 7) return 8
    else {
        return chars[s]!!
    }
}

fun compareLetters(s1: String, s2: String) : Int {
    val set1 = s1.toSet()
    val set2 = s2.toSet()

    var sum = 0
    for (ss in set1) {
        if (set2.contains(ss)) sum++
    }

    return sum
}

fun main(args: Array<String>) {
    val lines = File("files/day8.txt").readLines()

    var sum = 0
    for (line in lines) {
        val input = line.split(" | ")[0].split(" ").map { it.toCharArray().sorted().joinToString("") }
        val output = line.split(" | ")[1].split(" ").map { it.toCharArray().sorted().joinToString("") }

        val lFive = input.filter { it.length == 5 }
        val lSix = input.filter { it.length == 6 }

        val chars = mutableMapOf<String, Int>()

        for (six in lSix) {
            if (compareLetters(six, lFive[0]) == 4 && compareLetters(six, lFive[1]) == 4 && compareLetters(six, lFive[2]) == 4) {
                chars[six] = 0
                println(chars)
            }
        }

        for (six in lSix) {
            if (six in chars.keys) continue

            var times5 = 0
            var times4 = 0
            var times5Str = mutableListOf<String>()
            var times4Str = mutableListOf<String>()

            for (five in lFive) {
                if (compareLetters(six, five) == 5) {
                    times5++
                    times5Str.add(five)
                }
                else if (compareLetters(six, five) == 4) {
                    times4++
                    times4Str.add(five)
                }
            }

            if (times5 == 1) {
                chars[six] = 6
                chars[times5Str[0]] = 5
                println(chars)
            }

            if (times4 == 1) {
                chars[six] = 9
                chars[times4Str[0]] = 2
                println(chars)
            }
        }

        for (five in lFive) {
            if (!(five in chars.keys)) {
                chars[five] = 3
                println(chars)
                break
            }
        }

        println(chars)

        var number = 0
        number += 1000 * f(output[0], chars)
        number += 100 * f(output[1], chars)
        number += 10 * f(output[2], chars)
        number += 1 * f(output[3], chars)

        sum += number
    }

    println(sum)
    println("Hello World on day8!")
}
