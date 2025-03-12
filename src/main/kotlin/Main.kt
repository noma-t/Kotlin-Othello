package io.github.nomat


fun main() {
    println("Hello World!")
    Board.printBoard()
    
    while (true) {
        println("Please input x and y")
        val x = readln().toInt()
        val y = readln().toInt()
        if (Board.putDisc(x, y) == 1) {
            println("You can't put a disc there")
            continue
        }
        Board.printBoard()
    }
}
