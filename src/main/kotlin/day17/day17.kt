package day17

import java.lang.Long.max

fun simulate(velX: Int, velY: Int): Long {
    val leftX: Long = 56
    val rightX: Long = 76
    val topY: Long = -134
    val botY: Long = -162

    var startX: Long = 0
    var startY: Long = 0
    var velocityX: Long = velX.toLong()
    var velocityY: Long = velY.toLong()
    var maxY = startY

    while (true) {
        if (startX in leftX..rightX && startY <= topY && startY >= botY) {
            return maxY
        } else if (startX > rightX || (startX < leftX && velocityX == 0L) || (startY < botY && velocityY < 0)) {
            return -1
        }

        startX += velocityX
        startY += velocityY
        maxY = max(maxY, startY)

        velocityX = max(velocityX - 1, 0)
        velocityY--
    }
}

fun main(args: Array<String>) {
    println("Hello World on day17!")

    val maxes = mutableListOf<Long>()

    for (velX in 0..100) {
        for (velY in -162..1000) {
            val r = simulate(velX, velY)
            if (r >= 0) {
                maxes.add(r)
            }
        }
    }

    println(maxes.filter { it != -1L }.size)
    println(maxes.maxOf { it })
}
