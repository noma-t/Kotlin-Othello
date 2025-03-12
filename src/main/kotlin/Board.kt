package io.github.nomat

object Board {
    private var board: Array<ByteArray> = Array(8) { ByteArray(8) }
    init {
        board[3][3] = 1
        board[4][4] = 1
        board[3][4] = 2
        board[4][3] = 2
    }
    val directions: Array<Direction> = arrayOf(
        Direction( 0,  1), // right
        Direction( 1,  1), // right-down
        Direction( 1,  0), // down
        Direction( 1, -1), // left-down
        Direction(-1,  0), // left
        Direction(-1, -1), // left-up
        Direction( 0, -1), // up
        Direction(-1,  1), // right-up
    )
    
    // true: White, false: Black
    var turn: Boolean = true
    var lastPutPlace = Pair(-1, -1)
    
    
    fun printBoard() {
        println("+---+---+---+---+---+---+---+---+")
        for (row in board) {
            for (cell in row) {
                print("| ")
                when (cell) {
                    1.toByte() -> print("W ")
                    2.toByte() -> print("B ")
                    else -> print("  ")
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
        return if (directions.any { checkPlaceableDirected(x, y, it) == 0 }) 0 else 1
    }
    fun checkPlaceableDirected(x: Int, y: Int, direction: Direction): Int {
        val myColor: Byte = if (turn) 1 else 2
        val enemyColor: Byte = if (turn) 2 else 1
        
        var xx = x
        var yy = y

        for (i in 0..6) {
            xx += direction.x
            yy += direction.y
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
        // Check 8 directions
        directions.forEach { flipDisksDirected(it) }
    }
    fun flipDisksDirected(direction: Direction) {
        if (checkPlaceableDirected(lastPutPlace.first, lastPutPlace.second, direction) == 1) {
            return
        }
        var x: Int = lastPutPlace.first
        var y: Int = lastPutPlace.second
        val myColor: Byte = if (turn) 1 else 2
        for (i in 0..6) {
            x += direction.x
            y += direction.y
            if (board[y][x] == myColor) {
                break
            }
            board[y][x] = myColor
        }
    }
}