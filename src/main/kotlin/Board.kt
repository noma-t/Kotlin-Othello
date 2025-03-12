package io.github.nomat

object Board {
    private var board: Array<ByteArray> = arrayOf(
        byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        byteArrayOf(0, 0, 0, 1, 2, 0, 0, 0),
        byteArrayOf(0, 0, 0, 2, 1, 0, 0, 0),
        byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
        byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
    )
    val directions: Array<Pair<Int, Int>> = arrayOf(
        Pair( 0,  1), // right
        Pair( 1,  1), // right-down
        Pair( 1,  0), // down
        Pair( 1, -1), // left-down
        Pair(-1,  0), // left
        Pair(-1, -1), // left-up
        Pair( 0, -1), // up
        Pair(-1,  1), // right-up
    )
    
    // true: White, false: Black
    var turn: Boolean = true
    
    var lastPutPlace = Pair(-1, -1)
    
    
    fun printBoard() {
        println("+---+---+---+---+---+---+---+---+")
        for (i in 0..7) {
            for (j in 0..7) {
                print("| ")
                when (board[i][j]) {
                    0.toByte() -> print("  ")
                    1.toByte() -> print("W ")
                    2.toByte() -> print("B ")
                }
            }
            println("|")
            println("+---+---+---+---+---+---+---+---+")
        }
    }
    
    fun changeTurn() {
        turn = !turn
    }

    fun putDisc(x: Int, y: Int): Int {
        if (board[y][x] != 0.toByte()) {
            return 1
        }
        if (checkPlaceableAllDirections(x, y) == 1) {
            return 1
        }
        board[y][x] = if (turn) 1 else 2
        lastPutPlace = Pair(x, y)
        flipDisks()
        changeTurn()
        return 0
    }
    
    fun checkPlaceableAllDirections(x: Int, y: Int): Int {
        for (direction in directions) {
            if (checkPlaceableDirected(x, y, direction) == 0) {
                return 0
            }
        }
        return 1
    }
    fun checkPlaceableDirected(x: Int, y: Int, direction: Pair<Int, Int>): Int {
        val myColor: Byte = if (turn) 1 else 2
        val enemyColor: Byte = if (turn) 2 else 1
        
        var xx = x
        var yy = y

        for (i in 0..6) {
            xx += direction.first
            yy += direction.second
            // 範囲外
            if (xx < 0 || xx > 7 || yy < 0 || yy > 7) {
                // println("out of range $xx $yy $i")
                break
            }
            // 隣が敵の色じゃない
            if (i == 0 && board[yy][xx] != enemyColor) {
                // println("not enemy $xx $yy $i")
                break
            }
            // 何も置かれていない
            if (board[yy][xx] == 0.toByte()) {
                // println("empty $xx $yy $i")
                break
            }
            // 自分の色が見つかった
            if (board[yy][xx] == myColor) {
                // println("found $xx $yy $i")
                return 0
            }
        }
        return 1
        
    }
    
    fun flipDisks() {
        val myColor: Byte = if (turn) 1 else 2
        val enemyColor: Byte = if (turn) 2 else 1
        
        // Check 8 directions
        for (direction in directions) {
            flipDisksDirected(direction)
        }
    }
    fun flipDisksDirected(direction: Pair<Int, Int>) {
        if (checkPlaceableDirected(lastPutPlace.first, lastPutPlace.second, direction) == 1) {
            return
        }
        var x: Int = lastPutPlace.first
        var y: Int = lastPutPlace.second
        val myColor: Byte = if (turn) 1 else 2
        for (i in 0..6) {
            x += direction.first
            y += direction.second
            if (board[y][x] == myColor) {
                break
            }
            board[y][x] = myColor
        }
    }
}