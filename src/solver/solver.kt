package solver

import models.*


/**
 * Try to found [State] matching the expected one, and return the history
 *
 * @param statesWithHistory list of state and history to check
 * @param expected the expected [State]
 * @return a list of [Move] or null if not found
 */
internal fun findSolution(statesWithHistory: Collection<StateWithHistory>, expected: State): List<Move>? =
    statesWithHistory
        .find { (state, _) -> state == expected }
        ?.second

/**
 * Computes next [State]s
 *
 * @param stateWithHistory a [StateWithHistory]
 * @return all next [StateWithHistory] with processing all available [Move] of [State]
 */
internal fun nextStatesFromState(stateWithHistory: StateWithHistory): List<StateWithHistory> {
    val (state, history) = stateWithHistory
    return state.availableMoves()
        .map { move -> state.process(move) to (history + move) }
}

/**
 * Determine next [State]s
 *
 * @param statesWithHistory last [StateWithHistory] list
 * @return all next [StateWithHistory]
 */
internal fun nextStatesFromCollection(statesWithHistory: Collection<StateWithHistory>): List<StateWithHistory> =
    statesWithHistory.flatMap { nextStatesFromState(it) }

/**
 * Compute all visited [State] with previous visited [State], and new ones
 *
 * @param visitedStates already visited [State]
 * @param newlyStates new [State]s
 * @return set of all visited [State]s, including the new ones
 */
internal fun allVisitedStates(visitedStates: Set<State>, newlyStates: List<StateWithHistory>): Set<State> =
    visitedStates + newlyStates.map { (state, _) -> state }

/**
 * Solve Water Pouring Puzzle
 *
 * @param from the initial [State]
 * @param to the expected [State]
 * @return list of [Move]s required to solve the puzzle
 * @throws IllegalStateException if no solution found
 */
fun solve(from: State, to: State): List<Move> {

    tailrec fun aux(statesWithHistory: Collection<StateWithHistory>, visitedStates: Set<State>): List<Move> {
        // Terminal case
        val result = findSolution(statesWithHistory, to)
        if (result != null) {
            return result
        }

        // next recursion parameters
        val nextStatesWithHistory = nextStatesFromCollection(statesWithHistory)
        val nextVisitedStates = allVisitedStates(visitedStates, nextStatesWithHistory)

        // Avoid infinite loop
        if (nextVisitedStates == visitedStates) {
            throw IllegalStateException("No solution found 😢")
        }

        // recursion
        return aux(nextStatesWithHistory, nextVisitedStates)
    }

    return aux(listOf(from to emptyList()), setOf(from))
}