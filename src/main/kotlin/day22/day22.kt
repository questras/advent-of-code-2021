package day22

import java.io.File
import java.lang.Long.max
import java.lang.Long.min
import kotlin.math.abs

data class Command(
    val on: Boolean,
    val xInv: Interval,
    val yInv: Interval,
    val zInv: Interval
)

data class Cuboid(
    val xInv: Interval,
    val yInv: Interval,
    val zInv: Interval,
    val negativeVolume: Boolean = false
) {
    val volume: Long = (if (negativeVolume) -1 else 1) *
            xInv.diff * yInv.diff * zInv.diff

    fun withOppositeVolume(): Cuboid {
        return Cuboid(xInv, yInv, zInv, !(this.negativeVolume))
    }

    override fun toString(): String {
        return "Interval([${xInv.left},${xInv.right}], [${yInv.left},${yInv.right}], [${zInv.left},${zInv.right}], $negativeVolume)"
    }
}

fun Cuboid.overlap(other: Cuboid): Boolean {
    return this.xInv.overlap(other.xInv) &&
            this.yInv.overlap(other.yInv) &&
            this.zInv.overlap(other.zInv)
}

fun Cuboid.commonCuboid(other: Cuboid): Cuboid? {
    if (!this.overlap(other)) {
        return null
    }

    return Cuboid(
        Interval(max(xInv.left, other.xInv.left), min(xInv.right, other.xInv.right)),
        Interval(max(yInv.left, other.yInv.left), min(yInv.right, other.yInv.right)),
        Interval(max(zInv.left, other.zInv.left), min(zInv.right, other.zInv.right)),
        this.negativeVolume
    )
}

data class Interval(
    val left: Long,
    val right: Long
) {
    val diff = abs(left - right) + 1
    fun overlap(other: Interval): Boolean {
        return (this.left <= other.left && this.right >= other.left) ||
                (other.left <= this.left && other.right >= this.left)
    }

}

fun main(args: Array<String>) {
    val lines = File("files/day22.txt").readLines()

    val commands = mutableListOf<Command>()
    for (line in lines) {
        val (type, rest) = line.split(" ")
        val (x, y, z) = rest.split(",")

        val xRange = x.substring(2).split("..")
        val yRange = y.substring(2).split("..")
        val zRange = z.substring(2).split("..")

        commands.add(
            Command(
                type == "on",
                Interval(
                    Integer.parseInt(xRange[0]).toLong(),
                    Integer.parseInt(xRange[1]).toLong()
                ),
                Interval(
                    Integer.parseInt(yRange[0]).toLong(),
                    Integer.parseInt(yRange[1]).toLong()
                ),
                Interval(
                    Integer.parseInt(zRange[0]).toLong(),
                    Integer.parseInt(zRange[1]).toLong()
                ),
            )
        )
    }

    val cuboids = mutableListOf<Cuboid>()
    for (command in commands) {
        val cuboid = Cuboid(command.xInv, command.yInv, command.zInv)

        var intersections = cuboids.mapNotNull { it.commonCuboid(cuboid)?.withOppositeVolume() }

        if (command.on) {
            cuboids.add(cuboid)
        }

        cuboids.addAll(intersections)
    }

    val sum = cuboids.sumOf { it.volume }
    println(sum)
}
