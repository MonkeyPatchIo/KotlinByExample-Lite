package main

import models.*
import solver.*
import tests.fwrk.*
import tests.*

fun main(args: Array<String>) {
    val help = args.contains("--help") || args.contains("-h")
    when {
        help           ->
            // Help
            println("Sample: java -jar target/main.jar \"0/5, 0/3\" \"4/5, 0/3\"")
        args.size == 2 ->
            // Solve
            args.map { it.toState() }
                .let { solveWaterPouring(it[0], it[1]) }
        else           ->
            // Test
            tests("Water Pouring", glassSpecs, solveSpecs)
                .runTest()
    }
}

private fun solveWaterPouring(start: State, end: State) {
    println("Solve $start -> $end")

    val result = solve(start, end)

    var state = start
    println(state)
    result.forEachIndexed { index, move ->
        state = state.process(move)
        println("#$index: $move -> ${state.toDisplayString()}")
    }
}