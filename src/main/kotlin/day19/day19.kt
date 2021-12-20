package day19

import java.io.File
import java.util.*
import kotlin.math.abs

data class Position(
    val x: Int,
    val y: Int,
    val z: Int
)

operator fun Position.minus(other: Position) =
    Position(
        this.x - other.x,
        this.y - other.y,
        this.z - other.z,
    )

operator fun Position.plus(other: Position) =
    Position(
        this.x + other.x,
        this.y + other.y,
        this.z + other.z,
    )

fun Set<Position>.relativeTo(position: Position): Set<Position> {
    return this.map { it - position }.toSet()
}

data class Scanner(
    val id: Int,
    val beacons: Set<Position>,
    val scannersPositions: Set<Position> = setOf()
)

data class ScannerOverlap(
    val beacons: Set<Position>,
    val relativeStart: Position,
    val newScannerPosition: Position
)

fun Set<Position>.applyRotations(): List<Set<Position>> =
    listOf(
        this.map { Position(it.x, it.y, it.z) }.toSet(),
        this.map { Position(it.x, it.z, it.y) }.toSet(),
        this.map { Position(it.y, it.x, it.z) }.toSet(),
        this.map { Position(it.y, it.z, it.x) }.toSet(),
        this.map { Position(it.z, it.x, it.y) }.toSet(),
        this.map { Position(it.z, it.y, it.x) }.toSet(),
    )

fun Set<Position>.applyTransformations(): List<Set<Position>> =
    listOf(
        this.map { Position(it.x, it.y, it.z) }.toSet(),
        this.map { Position(-it.x, it.y, it.z) }.toSet(),
        this.map { Position(it.x, -it.y, it.z) }.toSet(),
        this.map { Position(it.x, it.y, -it.z) }.toSet(),
        this.map { Position(-it.x, -it.y, -it.z) }.toSet(),
        this.map { Position(it.x, -it.y, -it.z) }.toSet(),
        this.map { Position(-it.x, it.y, -it.z) }.toSet(),
        this.map { Position(-it.x, -it.y, it.z) }.toSet(),
    )

fun findOverlap(scanner1: Scanner, scanner2: Scanner): ScannerOverlap? {
    val MINIMAL_INTERSECT_SIZE = 12

    val beacons1 = scanner1.beacons
    val beacons2Possibilities = scanner2.beacons.applyRotations().flatMap { it.applyTransformations() }

    for (beacons2 in beacons2Possibilities) {
        for (beacon1 in beacons1) {
            for (beacon2 in beacons2) {
                val positions1 = beacons1.relativeTo(beacon1).toSet()
                val positions2 = beacons2.relativeTo(beacon2).toSet()

                if (positions1.intersect(positions2).size >= MINIMAL_INTERSECT_SIZE) {
                    val scanner2Pos = Position(beacon1.x - beacon2.x, beacon1.y - beacon2.y, beacon1.z - beacon2.z)
                    return ScannerOverlap(positions2, beacon1, scanner2Pos)
                }
            }
        }
    }

    return null
}

fun Scanner.withNewBeacons(overlap: ScannerOverlap): Scanner {
    val newBeacons = this.beacons + overlap.beacons.map { it + overlap.relativeStart }
    return Scanner(this.id, newBeacons, this.scannersPositions + setOf(overlap.newScannerPosition))
}

fun main(args: Array<String>) {
    var lines = File("files/day19.txt").readLines().filter { it != "" }
    lines = lines.subList(1, lines.size)

    val scanners = mutableListOf<Scanner>()
    var currentBeacons = mutableSetOf<Position>()
    var id = 0
    for (line in lines) {
        if (line.substring(0, 2) == "--") {
            scanners.add(Scanner(id, currentBeacons))
            id++
            currentBeacons = mutableSetOf()
        } else {
            val values = line.split(",").map { Integer.parseInt(it) }
            currentBeacons.add(Position(values[0], values[1], values[2]))

            if (line == lines.last()) {
                scanners.add(Scanner(id, currentBeacons))
            }
        }
    }

    var anchorScanner = Scanner(scanners[0].id, scanners[0].beacons, setOf(Position(0, 0, 0)))
    val scannerQueue: Queue<Scanner> = LinkedList(scanners.subList(1, scanners.size))
    var prevScannerQueue: Queue<Scanner> = LinkedList()

    while (prevScannerQueue != scannerQueue && scannerQueue.isNotEmpty()) {
        prevScannerQueue = LinkedList(scannerQueue)

        var queueItemsLeft = scannerQueue.size
        while (queueItemsLeft > 0) {
            val currentScanner = scannerQueue.poll()
            val overlap = findOverlap(anchorScanner, currentScanner)
            if (overlap == null) {
                scannerQueue.add(currentScanner)
            } else {
                anchorScanner = anchorScanner.withNewBeacons(overlap)
            }

            queueItemsLeft--
        }
    }

    // part 1
    println(anchorScanner.beacons.size)

    // part 2
    val diffs = anchorScanner
        .scannersPositions
        .flatMap { s1 ->
            anchorScanner.scannersPositions
                .map { s2 -> abs(s1.x - s2.x) + abs(s1.y - s2.y) + abs(s1.z - s2.z) }
        }
    println(diffs.maxOf { it })

    println("Hello World on day19!")
}
