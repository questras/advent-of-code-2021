package day24

import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

fun Boolean.toInt(): Int = if (this) 1 else 0

class Solver {
    companion object {
        var part = 1
    }
}

fun funValue(i: Int, previousZ: Int, inputW: Int): Int {
    val a = listOf(0, 1, 1, 1, 26, 1, 26, 1, 1, 1, 26, 26, 26, 26, 26)
    val b = listOf(0, 15, 12, 13, -14, 15, -7, 14, 15, 15, -7, -8, -7, -5, -10)
    val c = listOf(0, 15, 5, 6, 7, 9, 6, 14, 3, 1, 3, 4, 6, 7, 1)

    var z = previousZ
    var x = 0
    x += z
    x = x % 26

    z = z / a[i]
    x = x + b[i]

    x = (x == inputW).toInt()
    x = (x == 0).toInt()

    var y = 0
    y = y + 25
    y = y * x
    y = y + 1
    z = z * y
    y = y * 0
    y = y + inputW

    y = y + c[i]

    y = y * x
    z = z + y
    return z
}


data class Key(
    val inputW: Int,
    val previousZ: Int
)

class Memory {
    companion object {
        private var memory = mutableMapOf<Pair<Int, Int>, List<Int>>()

        fun getMem(level: Int, sought: Int): List<Int>? {
            return memory[Pair(level, sought)]
        }

        fun setMem(level: Int, sought: Int, lst: List<Int>) {
            memory[Pair(level, sought)] = lst
        }

        fun resetMem() {
            memory = mutableMapOf<Pair<Int, Int>, List<Int>>()
        }
    }
}

fun List<Int>.toLong(): Long {
    return this.joinToString("").toLong()
}

fun findBestResult(results: List<List<Int>>): List<Int> {
    val coefficient = if (Solver.part == 1) -1 else 1
    return results.map { it.toLong() to it }.sortedBy { coefficient * it.first }.first().second
}

fun solve(level: Int, sought: Int, maps: MutableList<MutableMap<Key, Int>>): List<Int> {
    val possibleOutcomes = maps[level - 1].filter { it.value == sought }.map { it.key }.sortedBy { -it.inputW }

    if (level == 1) {
        val possibleOutcomes2 = possibleOutcomes.filter { it.previousZ == 0 }
        if (possibleOutcomes2.isEmpty()) return listOf()
        return listOf(possibleOutcomes2.first().inputW)
    }

    val mem = Memory.getMem(level, sought)
    if (mem != null) return mem

    val results = mutableListOf<List<Int>>()
    for (possibleOutcome in possibleOutcomes) {
        val lst = solve(level - 1, possibleOutcome.previousZ, maps)
        if (lst.size == level - 1) {
            val result = lst + listOf(possibleOutcome.inputW)
            results.add(result)
        }
    }

    if (results.isNotEmpty()) {
        val bestResult = findBestResult(results)
        Memory.setMem(level, sought, bestResult)
        return bestResult
    }

    Memory.setMem(level, sought, listOf())
    return listOf()
}

@OptIn(ExperimentalTime::class)
fun main(args: Array<String>) {
    val leftRange = 0
    val rightRange = 100000000
    val maps = mutableListOf<MutableMap<Key, Int>>()
    for (i in 1..14) {
        val map = mutableMapOf<Key, Int>()
        var previousValues = if (i == 1) {
            setOf(0)
        } else {
            maps[i - 2].values.toSet()
        }

        for (z in previousValues.filter { it in leftRange..rightRange }) {
            for (w in 1..9) {
                val f = funValue(i, z, w)
                map[Key(w, z)] = f
            }
        }
        maps.add(map)
    }
    println("Map calculated, start solving")

    println("Part1:")
    Solver.part = 1
    val elapsed: Duration = measureTime {
        println(solve(14, 0, maps).joinToString(""))
    }
    println(elapsed.inWholeSeconds)

    println("Part2:")
    Solver.part = 2
    Memory.resetMem()
    val elapsed2: Duration = measureTime {
        println(solve(14, 0, maps).joinToString(""))
    }
    println(elapsed2.inWholeSeconds)
}
