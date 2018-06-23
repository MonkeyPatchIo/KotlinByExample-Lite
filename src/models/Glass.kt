package models




/**
 * A Glass POJO
 */
data class Glass(val capacity: Int, val current: Int = 0) {

    init {
        require(capacity > 0) { "Capacity: $capacity should be > 0" }
        require(current in 0..capacity) { "Current: $current should be into [0, $capacity]" }
    }
}


/**
 * Create a Glass from a [String]
 */
fun String.toGlass(): Glass {
    val match = "(\\d*)/(\\d*)".toRegex().matchEntire(this) ?: throw IllegalArgumentException("Does not match x/y pattern")
    val current = match.groups[1]?.value?.toInt() ?: throw IllegalArgumentException("Does not match x/y capacity")
    val capacity = match.groups[2]?.value?.toInt() ?: throw IllegalArgumentException("Does not match x/y capacity")
    return Glass(capacity = capacity, current = current)
}
