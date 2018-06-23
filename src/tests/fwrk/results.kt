package tests.fwrk

sealed class TestResult {

    fun asString(test: Test, prefix: String = ""): String {
        val base = "$prefix$status ${test.name}"

        return when (this) {
            is Success     -> base
            is Skipped     -> base
            is Failure     -> "$base: $cause"
            is Error       -> "$base: $cause"
            is TestResults ->
                "$base\n" + results.joinToString("\n") { (test, res) ->
                    res.asString(test, "$prefix  ")
                }
        }
    }

    private val ok: Boolean by lazy {
        when (this) {
            is Success     -> true
            is Skipped     -> true
            is Failure     -> false
            is Error       -> false
            is TestResults ->
                results.fold(true) { acc, (_, res) -> acc && (res.ok) }
        }
    }

    private val status: String by lazy {
        when (this) {
            is Success     -> "✅"
            is Skipped     -> "⚠️"
            is Failure     -> "❌"
            is Error       -> "💥"
            is TestResults ->
                if (results.all { ok }) "✅"
                else "❌"
        }
    }
}

object Success : TestResult()
object Skipped : TestResult()
data class Failure(val cause: String) : TestResult()
data class Error(val cause: Throwable) : TestResult()
data class TestResults(val results: List<Pair<Test, TestResult>>) : TestResult()