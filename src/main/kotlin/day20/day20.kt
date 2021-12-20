package day20

import java.io.File

fun calculateForPixel(x: Int, y: Int, image: List<String>): Int {
    val points = listOf(
        Pair(x - 1, y - 1),
        Pair(x, y - 1),
        Pair(x + 1, y - 1),
        Pair(x - 1, y),
        Pair(x, y),
        Pair(x + 1, y),
        Pair(x - 1, y + 1),
        Pair(x, y + 1),
        Pair(x + 1, y + 1),
    )

    val binaryNumber =
        points.map { image[it.second][it.first].let { c -> if (c == '.') "0" else "1" } }.joinToString("")
    return Integer.parseInt(binaryNumber, 2)
}

fun decodeImage(image: List<String>, algorithm: String, dotBackground: Boolean): List<String> {
    val c = if (dotBackground) "." else "#"
    val newRow = c.repeat(image[0].length + 6)
    val extendedImage =
        listOf(newRow, newRow, newRow) +
                image.map { "$c$c$c$it$c$c$c" } +
                listOf(newRow, newRow, newRow)

    val newImage = mutableListOf<MutableList<String>>()
    for (y in 1 until extendedImage.size - 1) {
        newImage.add(mutableListOf())
        for (x in 1 until extendedImage[0].length - 1) {
            newImage.last().add(algorithm[calculateForPixel(x, y, extendedImage)].toString())
        }
    }

    return newImage.map { it.joinToString("") }
}

fun List<String>.printLitPixelsAmount() {
    println(this.map { s -> s.toCharArray().filter { it == '#' }.size }.sum())
}

fun main(args: Array<String>) {
    val lines = File("files/day20.txt").readLines()
    val algorithm = lines[0]

    var image = lines.subList(2, lines.size)
    for (i in 1..50) {
        image = decodeImage(image, algorithm, i % 2 == 1)
        if (i == 2 || i == 50) {
            image.printLitPixelsAmount()
        }
    }

    println("Hello World on day20!")
}
