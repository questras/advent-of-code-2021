package day15

import java.io.File
import java.util.*

data class Point(
    val x: Int,
    val y: Int
)

data class Graph<T>(
    val vertices: Set<T>,
    val edges: Map<T, Set<T>>,
    val weights: Map<Pair<T, T>, Int>
)

fun <T> dijkstra(graph: Graph<T>, start: T): Map<T, T?> {
    val S: MutableSet<T> = mutableSetOf() // a subset of vertices, for which we know the true distance

    val delta = graph.vertices.map { it to Int.MAX_VALUE }.toMap().toMutableMap()
    delta[start] = 0

    val previous: MutableMap<T, T?> = graph.vertices.map { it to null }.toMap().toMutableMap()

    val compareByShortestPath: Comparator<Pair<T, Int>> = compareBy { it.second }
    val priorityQueue = PriorityQueue<Pair<T, Int>>(compareByShortestPath)
    for (d in delta) {
        priorityQueue.add(Pair(d.key, d.value))
    }

    while (!priorityQueue.isEmpty()) {
        var v = priorityQueue.poll()!!.first
        while (S.contains(v)) {
            if (priorityQueue.isEmpty()) {
                break
            }
            v = priorityQueue.poll()!!.first
        }

        graph.edges.getValue(v).minus(S).forEach { neighbor ->
            val newPath = delta.getValue(v) + graph.weights.getValue(Pair(v, neighbor))

            if (newPath < delta.getValue(neighbor)) {
                delta[neighbor] = newPath
                priorityQueue.add(Pair(neighbor, newPath))
                previous[neighbor] = v
            }
        }

        S.add(v)
    }

    return previous.toMap()
}

fun <T> shortestPath(shortestPathTree: Map<T, T?>, start: T, end: T): List<T> {
    fun pathTo(start: T, end: T): List<T> {
        if (shortestPathTree[end] == null) return listOf(end)
        return listOf(pathTo(start, shortestPathTree[end]!!), listOf(end)).flatten()
    }

    return pathTo(start, end)
}

fun getAdjacent(row: Int, col: Int, maxX: Int, maxY: Int): List<Point> {
    val points = listOf<Point>(
        Point(row - 1, col),
        Point(row + 1, col),
        Point(row, col - 1),
        Point(row, col + 1),
    ).filter { it.x >= 0 && it.y >= 0 && it.x < maxX && it.y < maxY }

    return points
}

fun main(args: Array<String>) {
    val startLines = File("files/day15.txt").readLines().map { it.toCharArray().map { c -> Integer.parseInt(c.toString()) }}

    val size = 100

    val maxX = size* 5
    val maxY = size * 5

    val lines: MutableList<MutableList<Int>> = mutableListOf()
    for (y in 0 until maxY) {
        lines.add(mutableListOf())
        for (x in 0 until maxX) {
            val xMod = x - (x / size) * size
            val yMod = y - (y / size) * size

            var calculated = startLines[yMod][xMod]

            for (s in 1..(x / size + y / size)) {
                calculated += 1
                if (calculated == 10) {
                    calculated = 1
                }
            }

            lines[y].add(calculated)

        }
    }

    val start = Point(0, 0)
    val stop = Point(maxX-1, maxY-1)

    val vertices = mutableSetOf<Point>()
    for (i in 0 until maxX) {
        for (k in 0 until maxY) {
            vertices.add(Point(i, k))
        }
    }

    val edges = mutableMapOf<Point, Set<Point>>()
    for (point in vertices) {
        val adj = getAdjacent(point.x, point.y, maxX, maxY)
        edges[point] = adj.toSet()
    }

    val weights = mutableMapOf<Pair<Point, Point>, Int>()
    for (point in vertices) {
        val neighbours = edges[point]!!

        for (n in neighbours) {
            weights[Pair(point, n)] = lines[n.y][n.x]
        }
    }

    val graph = Graph(vertices, edges, weights)

    val shortestPathTree = dijkstra(graph, start)
    val shortestPath = shortestPath(shortestPathTree, start, stop)

    var sum: Long = 0
    for (p in shortestPath) {
        sum += lines[p.y][p.x]
    }

    println(sum - lines[0][0])

    println("Hello World on day15!")
}
