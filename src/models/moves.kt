package models


/**
 * All Moves
 */
sealed class Move

/** Empty a glass */
data class Empty(val index: Int) : Move()

/** Fill a glass */
data class Fill(val index: Int) : Move()

/** Pour a glass into another glass */
data class Pour(val from: Int, val to: Int) : Move() {
    init {
        require(from != to)
    }
}