package models


/**
 * Define a State like an alias of List<Glass>
 */
typealias State = List<Glass>


/**
 * Create a State from a [String]
 */
fun String.toState(): State =
    this.split(",")
        .map { it.trim() }
        .map { it.toGlass() }

/**
 * Display String
 *
 * @return a String representation for display the [State]
 */
fun State.toDisplayString(): String =
    this.joinToString(", ", prefix = "[", postfix = "]") { "${it.current}/${it.capacity}" }
