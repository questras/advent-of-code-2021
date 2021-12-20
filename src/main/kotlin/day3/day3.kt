package day3

import java.io.File

fun howManyOnIndex(lst: MutableList<String>, index: Int, c: Char = '1'): Int {
    var sum = 0
    for (i in lst) {
        if (i[index] == c) sum++
    }

    return sum
}

fun takeWithCharOnIndex(lst: MutableList<String>, index: Int, c: Char): MutableList<String> {
    val newLst = mutableListOf<String>()
    for (i in lst) {
        if (i[index] == c) newLst.add(i)
    }

    return newLst
}

fun main(args: Array<String>) {
    val arr = File("files/day3.txt").readLines().toMutableList()

    val s1: MutableList<Char> = mutableListOf()
    val s2: MutableList<Char> = mutableListOf()

    for (i in 0 until arr[0].length) {
        if (howManyOnIndex(arr, i, '1') >= ((arr.size + 1) / 2)) {
            s1.add('1')
            s2.add('0')
        }
        else {
            s1.add('0')
            s2.add('1')
        }
    }
    println(Integer.parseInt(s1.joinToString(""), 2) * Integer.parseInt(s2.joinToString(""), 2))

    val bins = mutableListOf<String>()
    var curr = arr
    for (i in 0 until curr[0].length) {
        if (curr.size == 1) {
            break
        }

        curr = if (howManyOnIndex(curr, i, '1') >= ((curr.size + 1)  / 2)) {
            takeWithCharOnIndex(curr, i, '1')
        } else {
            takeWithCharOnIndex(curr, i, '0')
        }
    }
    bins.add(curr[0])

    curr = arr
    for (i in 0 until curr[0].length) {
        if (curr.size == 1) {
            break
        }

        curr = if (howManyOnIndex(curr, i, '1') < ((curr.size + 1) / 2)) {
            takeWithCharOnIndex(curr, i, '1')
        } else {
            takeWithCharOnIndex(curr, i, '0')
        }
    }
    bins.add(curr[0])

    println(Integer.parseInt(bins[0], 2) * Integer.parseInt(bins[1], 2))
}
