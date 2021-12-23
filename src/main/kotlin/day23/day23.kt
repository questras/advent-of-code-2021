package day23

import kotlin.math.max
import kotlin.math.min

enum class Type {
    AMBER, BRONZE, COPPER, DESERT
}

typealias Point = Pair<Int, Int>

data class Amphipod(
    val type: Type,
    val position: Point
) {
    val energyForMove = when (type) {
        Type.AMBER -> 1
        Type.BRONZE -> 10
        Type.COPPER -> 100
        Type.DESERT -> 1000
    }

    override fun toString(): String {
        return "$type (${position.first},${position.second})"
    }

    fun clone(): Amphipod {
        return Amphipod(this.type, Point(this.position.first, this.position.second))
    }
}

fun List<Amphipod>.positions(): List<Point> = this.map { it.position }

data class Board(
    val amphipods: List<Amphipod>,
    val roomSize: Int
) {
    val roomsByType = mapOf(
        Type.AMBER to List(roomSize) { Point(2, it - 2) }.reversed(),
        Type.BRONZE to List(roomSize) { Point(4, it - 2) }.reversed(),
        Type.COPPER to List(roomSize) { Point(6, it - 2) }.reversed(),
        Type.DESERT to List(roomSize) { Point(8, it - 2) }.reversed(),
    )

    val amphipodByPosition: MutableMap<Point, Amphipod> =
        amphipods
            .groupBy { it.position }
            .mapValues { entry -> entry.value.first() } as MutableMap<Point, Amphipod>

    fun getAmphipodList(): List<Amphipod> = amphipodByPosition.map { it.value }.sortedBy { it.position.first }

    override fun toString(): String {
        return "${getAmphipodList()}"
    }

    fun isWinning(): Boolean {
        return !roomsByType[Type.AMBER]!!.any { amphipodByPosition[it] == null || amphipodByPosition[it]!!.type != Type.AMBER } &&
                !roomsByType[Type.BRONZE]!!.any { amphipodByPosition[it] == null || amphipodByPosition[it]!!.type != Type.BRONZE } &&
                !roomsByType[Type.COPPER]!!.any { amphipodByPosition[it] == null || amphipodByPosition[it]!!.type != Type.COPPER } &&
                !roomsByType[Type.DESERT]!!.any { amphipodByPosition[it] == null || amphipodByPosition[it]!!.type != Type.DESERT }
    }

    fun moveAmphipod(oldPosition: Point, newPosition: Point) {
        val amp = this.amphipodByPosition[oldPosition]!!
        this.amphipodByPosition.remove(oldPosition)
        this.amphipodByPosition[newPosition] = Amphipod(amp.type, newPosition)
    }

    fun positionsToMove(amphipod: Amphipod): List<Point> {
        if (isInCorrectPosition(amphipod)) return listOf()

        val reachablePositions = mutableListOf<Point>()

        // If it's on top, it can only move to its room, if it contains only
        // its members or it's empty. Also there needs to be free path.
        if (isInTopRow(amphipod)) {
            val pt = canEnterItsRoom(amphipod)
            if (pt != null) return listOf(pt)
        } else { // in room
            if (isInRoomButBlocked(amphipod)) return reachablePositions

            // not blocked
            reachablePositions.addAll(getAvailableTopPoints(amphipod))

            if (!isInOwnRoom(amphipod)) {
                val pt = canEnterItsRoom(amphipod)
                if (pt != null) return listOf(pt)
            }
        }

        return reachablePositions
    }

    // Assuming that amphipod is in a room
    private fun getAvailableTopPoints(amphipod: Amphipod): List<Point> {
        return listOf(0, 1, 3, 5, 7, 9, 10).map { Point(it, 0) }.filter { isCleanPath(amphipod.position, it) }
    }

    private fun isInRoomButBlocked(amphipod: Amphipod): Boolean {
        return isInRoom(amphipod) &&
                getAmphipodList()
                    .map { it.position }
                    .any { it.first == amphipod.position.first && it.second > amphipod.position.second }
    }

    private fun isInRoom(amphipod: Amphipod): Boolean {
        return amphipod.position in roomsByType.values.flatten()
    }

    private fun isInOwnRoom(amphipod: Amphipod): Boolean {
        return amphipod.position in roomsByType[amphipod.type]!!
    }

    private fun canEnterItsRoom(amphipod: Amphipod): Point? {
        if (!isRoomEnterable(amphipod.type)) {
            return null
        }

        for (pointInRoom in roomsByType[amphipod.type]!!.reversed()) {
            if (isCleanPath(amphipod.position, pointInRoom)) {
                return pointInRoom
            }
        }

        return null
    }

    private fun isCleanPath(start: Point, stop: Point): Boolean {
        return getPointsOnPath(start, stop).toSet().intersect(getAmphipodList().positions().toSet()).isEmpty()
    }

    private fun isInCorrectPosition(amphipod: Amphipod): Boolean {
        if (!isInOwnRoom(amphipod)) return false


        for (point in roomsByType[amphipod.type]!!.reversed()) {
            val amp = amphipodByPosition[point]
            if (amp == null || amp.type != amphipod.type) {
                return false
            }

            if (amp == amphipod) {
                return true
            }
        }

        return true
    }

    private fun isRoomEnterable(type: Type): Boolean {
        val roomPositions = roomsByType[type]!!.toSet()
        val foreignAmphipods = getAmphipodList().filter { it.type != type }.positions().toSet()

        return foreignAmphipods.intersect(roomPositions).isEmpty()
    }

    private fun isInTopRow(amphipod: Amphipod): Boolean {
        return amphipod.position.second == 0
    }

    fun energyNeededToMove(amphipod: Amphipod, dest: Point): Long {
        return getPointsOnPath(amphipod.position, dest).size * amphipod.energyForMove.toLong()
    }

    private fun getPointsOnPath(point1: Point, point2: Point): List<Point> {
        val path = mutableListOf<Point>()

        // Bottom to Bottom
        if (point1.second < 0 && point2.second < 0) {
            val (leftPoint, rightPoint) = if (point1.first > point2.first) point2 to point1 else point1 to point2
            for (y in leftPoint.second..0) {
                path.add(Point(leftPoint.first, y))
            }
            for (x in leftPoint.first + 1..rightPoint.first - 1) {
                path.add(Point(x, 0))
            }
            for (y in 0 downTo rightPoint.second) {
                path.add(Point(rightPoint.first, y))
            }

        } else {

            // Top to bottom
            val (topPoint, botPoint) = if (point1.second > point2.second) Pair(point1, point2) else Pair(point2, point1)


            val maxFirst = max(topPoint.first, botPoint.first)
            val minFirst = min(topPoint.first, botPoint.first)
            val maxSecond = max(topPoint.second, botPoint.second)
            val minSecond = min(topPoint.second, botPoint.second)

            for (x in minFirst..maxFirst) {
                path.add(Point(x, topPoint.second))
            }

            for (y in minSecond until maxSecond) {
                path.add(Point(botPoint.first, y))
            }
        }

        path.remove(point1)
        return path
    }

    fun clone(): Board {
        return Board(
            getAmphipodList().map { it.clone() },
            roomSize
        )
    }

    companion object {
        fun test() {
            println("*****************Test start")
            val b1 = Board(listOf(), 1)
            println(
                b1.getPointsOnPath(Point(0, 0), Point(4, -2)).sortedBy { it.first } == listOf(
                    Point(1, 0),
                    Point(2, 0),
                    Point(3, 0),
                    Point(4, 0),
                    Point(4, -2),
                    Point(4, -1),
                )
            )
            println(
                b1.getPointsOnPath(Point(10, 0), Point(8, -2)).sortedBy { it.first } == listOf(
                    Point(8, 0),
                    Point(8, -2),
                    Point(8, -1),
                    Point(9, 0),
                    )
            )
            println(
                b1.getPointsOnPath(Point(6, -2), Point(8, -2)).sortedBy { it.first } == listOf(
                    Point(6, -1),
                    Point(6, 0),
                    Point(7, 0),
                    Point(8, 0),
                    Point(8, -1),
                    Point(8, -2)
                )
            )

            // blocked
            val b2 = Board(listOf(Amphipod(Type.COPPER, Point(0, 0)), Amphipod(Type.COPPER, Point(1, 0))), 2)
            println(b2.positionsToMove(b2.amphipodByPosition[Point(0, 0)]!!) == emptyList<Point>())

            println("*****************Test Stop")
        }
    }

}

class Memory {
    companion object {
        private val memory = mutableMapOf<MutableMap<Point, Amphipod>, Long>()

        fun getFromMemory(board: MutableMap<Point, Amphipod>): Long? = memory[board]

        fun putToMemory(board: MutableMap<Point, Amphipod>, value: Long) {
            if (memory[board] == null) {
                memory[board] = value
            }
            else {
                memory[board] = min(memory[board]!!, value)
            }
        }
    }
}

class Printer {
    companion object {
        var indent = 0

        fun printIndent(str: String) {
            repeat(indent) { print("\t") }
            println(str)
        }
    }
}

fun getMinimalEnergy(board: Board): Long? {
    val saved = Memory.getFromMemory(board.amphipodByPosition)

    if (saved != null) return if (saved == -1L) null else saved
    else {
        val results = mutableListOf<Long>()

        if (board.isWinning()) return 0L.also { Printer.printIndent("win!") }

        for (amphipod in board.getAmphipodList()) {
            Printer.indent ++
            Printer.printIndent("For $amphipod")
            for (newPosition in board.positionsToMove(amphipod)) {
                val tempBoard = board.clone()
                tempBoard.moveAmphipod(amphipod.position, newPosition)

                Printer.printIndent("$board")
                Printer.printIndent("to (Move $amphipod to $newPosition)")
                Printer.printIndent("$tempBoard")

                val toAdd = getMinimalEnergy(tempBoard)?.let {
                    if (it != -1L) it + board.energyNeededToMove(
                        amphipod,
                        newPosition
                    ) else null
                }
                toAdd?.let { results.add(it) }
            }
            Printer.indent--
        }

        val result = results.minOrNull()
        Memory.putToMemory(board.amphipodByPosition, result ?: -1)

        Printer.printIndent("Return result $result")
        return result
    }
}

fun main(args: Array<String>) {
    println("Hello World on day23!")

    println("Code does not work, eventually calculated result by hand.")
//    val correct = listOf(
//        Amphipod(Type.AMBER, Point(2, -2)),
//        Amphipod(Type.AMBER, Point(2, -1)),
//        Amphipod(Type.BRONZE, Point(4, -2)),
//        Amphipod(Type.BRONZE, Point(4, -1)),
//        Amphipod(Type.COPPER, Point(6, -2)),
//        Amphipod(Type.COPPER, Point(6, -1)),
//        Amphipod(Type.DESERT, Point(8, -2)),
//        Amphipod(Type.DESERT, Point(8, -1)),
//    )
//    val testBoard = Board(correct, 2)
//    println(getMinimalEnergy(testBoard) == 0L)
//    Board.test()

//    val amphipods = listOf(
//        Amphipod(Type.AMBER, Point(2, -2)),
//        Amphipod(Type.BRONZE, Point(2, -1)),
//        Amphipod(Type.DESERT, Point(4, -2)),
//        Amphipod(Type.COPPER, Point(4, -1)),
//        Amphipod(Type.AMBER, Point(6, -2)),
//        Amphipod(Type.DESERT, Point(6, -1)),
//        Amphipod(Type.BRONZE, Point(8, -2)),
//        Amphipod(Type.COPPER, Point(8, -1)),
//    )

//    val amphipods = listOf( // from task descr
//        Amphipod(Type.AMBER, Point(2, -2)),
//        Amphipod(Type.BRONZE, Point(2, -1)),
//        Amphipod(Type.DESERT, Point(4, -2)),
//        Amphipod(Type.COPPER, Point(4, -1)),
//        Amphipod(Type.COPPER, Point(6, -2)),
//        Amphipod(Type.BRONZE, Point(6, -1)),
//        Amphipod(Type.AMBER, Point(8, -2)),
//        Amphipod(Type.DESERT, Point(8, -1)),
//    )

//    val amphipods = listOf(
//        // Should return 460
//        Amphipod(Type.AMBER, Point(2, -2)),
//        Amphipod(Type.AMBER, Point(2, -1)),
//        Amphipod(Type.BRONZE, Point(4, -2)),
//        Amphipod(Type.COPPER, Point(4, -1)),
//        Amphipod(Type.COPPER, Point(6, -2)),
//        Amphipod(Type.BRONZE, Point(6, -1)),
//        Amphipod(Type.DESERT, Point(8, -2)),
//        Amphipod(Type.DESERT, Point(8, -1)),
//    )


//    val board = Board(amphipods, 2)
//    println(getMinimalEnergy(board))
//
//    val amphipods2 = listOf( // from task descr
//        Amphipod(Type.AMBER, Point(2, -4)),
//        Amphipod(Type.DESERT, Point(2, -3)),
//        Amphipod(Type.DESERT, Point(2, -2)),
//        Amphipod(Type.BRONZE, Point(2, -1)),
//
//        Amphipod(Type.DESERT, Point(4, -4)),
//        Amphipod(Type.BRONZE, Point(4, -3)),
//        Amphipod(Type.COPPER, Point(4, -2)),
//        Amphipod(Type.COPPER, Point(4, -1)),
//
//        Amphipod(Type.COPPER, Point(6, -4)),
//        Amphipod(Type.AMBER, Point(6, -3)),
//        Amphipod(Type.BRONZE, Point(6, -2)),
//        Amphipod(Type.BRONZE, Point(6, -1)),
//
//        Amphipod(Type.AMBER, Point(8, -4)),
//        Amphipod(Type.COPPER, Point(8, -6)),
//        Amphipod(Type.AMBER, Point(8, -2)),
//        Amphipod(Type.DESERT, Point(8, -1)),
//    )
//    val board2 = Board(amphipods2, 4)
//    println(getMinimalEnergy(board2))
}
