package tests.fwrk

fun test(name: String, inner: TestPlan.() -> TestPlan): Test =
    TestPlan(name).inner()

fun tests(name: String, vararg tests: Test): Test =
    SimpleTest(name) { testResults(tests.toList()) }

fun xtest(name: String, inner: TestPlan.() -> TestPlan): Test =
    SimpleTest(name) {Skipped}

private fun testResults(tests: Iterable<Test>): TestResults {
    val seed: List<Pair<Test, TestResult>> = emptyList()
    val results = tests.fold(seed) { acc, test ->
        acc + (test to test.test())
    }
    return TestResults(results)
}

sealed class Test {
    abstract val name: String
    abstract val code: () -> TestResult

    fun test(): TestResult =
        try {
            code()
        } catch (t: Throwable) {
            Error(t)
        }

    fun runTest() =
        println(test().asString(this))
}

internal data class SimpleTest(override val name: String,
                               override val code: () -> TestResult) : Test()

data class TestPlan internal constructor(override val name: String) : Test() {
    private val innerTests = mutableListOf<Test>()

    override val code: () -> TestResult
        get() = { testResults(innerTests) }

    fun describe(s: String, inner: TestPlan.() -> TestPlan): TestPlan {
        innerTests.add(TestPlan(s).inner())
        return this
    }

    fun xdescribe(s: String, inner: TestPlan.() -> TestPlan): TestPlan {
        val skipped = SimpleTest(s) { Skipped }
        innerTests.add(skipped)
        return this
    }

    fun it(s: String, inner: () -> TestResult): TestPlan {
        innerTests.add(SimpleTest(s, inner))
        return this
    }

    fun xit(s: String, inner: () -> TestResult): TestPlan {
        val skipped = SimpleTest(s) { Skipped }
        innerTests.add(skipped)
        return this
    }
}