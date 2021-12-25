package day18

import java.io.File

fun String.isNotNumber(): Boolean = this != "[" && this != "]" && this != ","

fun List<String>.printNice() {
    for (c in this) {
        print(c)
    }
    println()
}

fun explode(calc: List<String>): List<String> {
    val newCalc: MutableList<String> = calc.toMutableList()
    var bracketSum = 0
    for (idx in calc.indices) {
        if (calc[idx].isNotNumber()) {
            if (bracketSum >= 5) {
                val leftNumb = Integer.parseInt(calc[idx])
                val rightNumb = Integer.parseInt(calc[idx + 2])

                for (l in idx - 1 downTo 0) {
                    if (calc[l].isNotNumber()) {
                        newCalc[l] = (Integer.parseInt(calc[l]) + leftNumb).toString()
                        break
                    }
                }
                for (r in idx + 3 until calc.size) {
                    if (calc[r].isNotNumber()) {
                        newCalc[r] = (Integer.parseInt(calc[r]) + rightNumb).toString()
                        break
                    }
                }

                newCalc[idx] = "0"
                newCalc[idx + 1] = ""
                newCalc[idx + 2] = ""
                newCalc[idx + 3] = ""
                newCalc[idx - 1] = ""
                return newCalc.filter { it != "" }
            }
        } else if (calc[idx] == "[") {
            bracketSum++
        } else if (calc[idx] == "]") {
            bracketSum--
        }
    }

    return newCalc.filter { it != "" }
}

fun split(calc: List<String>): List<String> {
    var newCalc: MutableList<String> = calc.toMutableList()
    for (idx in calc.indices) {
        if (calc[idx].length > 1) {

            val numb = Integer.parseInt(calc[idx])
            val leftNumb = numb / 2
            val rightNumb = (numb + 1) / 2

            val newElement = mutableListOf("[", leftNumb.toString(), ",", rightNumb.toString(), "]")

            newCalc =
                (newCalc.subList(0, idx) + newElement + newCalc.subList(idx + 1, newCalc.size)) as MutableList<String>

            return newCalc.filter { it != "" }
        }
    }

    return newCalc.filter { it != "" }
}

fun magnitude(calc: List<String>): Long {
    var newCalc: MutableList<String> = calc.toMutableList()
    while (true) {
        for (idx in 0 until newCalc.size) {
            if (newCalc[idx] == "," && newCalc[idx - 1].isNotNumber() && newCalc[idx + 1].isNotNumber()) {
                val leftNumber = Integer.parseInt(newCalc[idx - 1]).toLong()
                val rightNumber = Integer.parseInt(newCalc[idx + 1]).toLong()

                newCalc[idx] = (3 * leftNumber + 2 * rightNumber).toString()
                newCalc[idx - 1] = ""
                newCalc[idx + 1] = ""
                newCalc[idx - 2] = ""
                newCalc[idx + 2] = ""
                newCalc = newCalc.filter { it != "" } as MutableList<String>
                break
            }
            if (idx == newCalc.size - 1) {
                return newCalc[0].toLong()
            }
        }
    }
}

fun calculate(lst1: List<String>, lst2: List<String>): List<String> {
    var newCalc = listOf<String>()
    newCalc = listOf("[") + lst1 + listOf(",") + lst2 + listOf("]")

    while (true) { // while there can be explosion/split
        var bracketSum = 0
        var wasExploded = false
        for (c in newCalc) {
            if (c.isNotNumber()) {
                if (bracketSum >= 5) {
                    newCalc = explode(newCalc)
                    wasExploded = true
                    break
                }
            } else if (c == "[") {
                bracketSum++
            } else if (c == "]") {
                bracketSum--
            }
        }

        if (wasExploded) continue

        var wasSplitted = false
        for (c in newCalc) {
            if (c.isNotNumber()) {
                if (c.length > 1) {
                    newCalc = split(newCalc)
                    wasSplitted = true
                    break
                }
            }
        }

        if (wasSplitted) continue
        break
    }

    return newCalc
}

fun main(args: Array<String>) {
    val lines = File("files/day18.txt").readLines().map { it.toCharArray().map { c -> c.toString() }.toList() }

    // part 1
    var calc = lines[0]
    for (element in lines.subList(1, lines.size)) {
        calc = calculate(calc, element)
    }
    println(magnitude(calc))

    // part 2
    val magnitudes = mutableListOf<Long>()

    for (element1 in lines) {
        for (element2 in lines) {
            if (element1 == element2) {
                continue
            }
            magnitudes.add(magnitude(calculate(element1, element2)))
        }
    }
    println(magnitudes.maxOf { it })

    println("Hello World on day18!")
}
