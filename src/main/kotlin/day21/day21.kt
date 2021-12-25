package day21

import java.lang.Long.max


class Dice {
    companion object {
        fun next(): Long {
            return roll() + roll() + roll()
        }

        private fun roll(): Long {
            val toReturn = number
            number++
            if (number > 100) {
                number = 1
            }

            rolls++
            return toReturn
        }

        var number = 1L
        var rolls = 0
    }
}

data class Player(
    var position: Long,
    var score: Long
) {
    fun move(byHowMany: Long) {
        val m = (position + byHowMany) % 10
        this.position = if (m == 0L) 10L else m
        this.score += this.position
    }
}

data class MemoryEntry(
    val player1: Player,
    val player2: Player,
    val player1Move: Boolean
)

class Memory {
    companion object {
        val memory = mutableMapOf<MemoryEntry, Pair<Long, Long>>()

        fun updateMemory(playerPair: MemoryEntry, valuePair: Pair<Long, Long>) {
            memory[playerPair] = valuePair
        }

        fun getFromMemory(playerPair: MemoryEntry): Pair<Long, Long>? {
            return memory[playerPair]
        }
    }
}

fun wins(player1: Player, player2: Player, player1Turn: Boolean): Pair<Long, Long> {
    val diceResults = listOf<Long>(3, 4, 5, 4, 5, 6, 5, 6, 7, 4, 5, 6, 5, 6, 7, 6, 7, 8, 5, 6, 7, 6, 7, 8, 7, 8, 9)

    if (player1.score >= 21) {
        return 1L to 0L
    } else if (player2.score >= 21) {
        return 0L to 1L
    }

    val memoryResult = Memory.getFromMemory(MemoryEntry(player1, player2, player1Turn))
    if (memoryResult != null) {
        return memoryResult
    }

    var myResult = 0L to 0L

    if (player1Turn) {
        for (res in diceResults) {
            val tempPlayer = Player(player1.position, player1.score)
            tempPlayer.move(res)
            val winResult = wins(tempPlayer, player2, false)
            myResult = Pair(myResult.first + winResult.first, myResult.second + winResult.second)
        }
    } else {
        for (res in diceResults) {
            val tempPlayer = Player(player2.position, player2.score)
            tempPlayer.move(res)
            val winResult = wins(player1, tempPlayer, true)
            myResult = Pair(myResult.first + winResult.first, myResult.second + winResult.second)
        }
    }

    Memory.updateMemory(MemoryEntry(player1, player2, player1Turn), myResult)
    return myResult
}

fun main(args: Array<String>) {
    val player1 = Player(1, 0)
    val player2 = Player(2, 0)

    while (player1.score < 1000 && player2.score < 1000) {
        var d = 0L
        d = Dice.next()
        player1.move(d)
        if (player1.score >= 1000) {
            println(player2.score * Dice.rolls)
            break
        }

        d = Dice.next()
        player2.move(d)
        if (player2.score >= 1000) {
            println(player1.score * Dice.rolls)
            break
        }
    }

    val player1p2 = Player(1, 0)
    val player2p2 = Player(2, 0)
    println(wins(player1p2, player2p2, true).let { max(it.first, it.second) })

    println("Hello World on day21!")
}
