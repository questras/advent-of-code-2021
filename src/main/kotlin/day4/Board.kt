package day4


data class Board(var board: MutableList<MutableList<Int>>) {
    public fun isAnyRowDone(): Boolean {
        for (row in 0 until board.size) {
            var isEmpty = true
            for (col in 0 until board[0].size) {
                if (board[row][col] != -1) {
                    isEmpty = false
                }
            }
            if (isEmpty) return true
        }

        return false
    }

    public fun isAnyColumnDone(): Boolean {
        for (col in 0 until board[0].size) {
            var isEmpty = true
            for (row in 0 until board.size) {
                if (board[row][col] != -1) {
                    isEmpty = false
                }
            }
            if (isEmpty) return true
        }

        return false
    }

    public fun getSumOfRest(): Int {
        var sum = 0
        for (row in 0 until board.size) {
            for (col in 0 until board[0].size) {
                if (board[row][col] != -1) {
                    sum += board[row][col]
                }
            }
        }

        return sum
    }

    public fun setNumber(number: Int) {
        for (row in 0 until board.size) {
            for (col in 0 until board[0].size) {
                if (board[row][col] == number) {
                    board[row][col] = -1
                }
            }
        }
    }
}