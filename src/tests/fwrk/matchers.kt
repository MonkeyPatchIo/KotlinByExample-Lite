package tests.fwrk

import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

// Standard matchers

infix fun <V> V.shouldBe(value: V): TestResult =
    if (this == value) Success
    else Failure("Expected '$value', got $this")

// Expect Failure matcher

inline fun <reified T : Throwable> shouldThrow(code: () -> Unit): TestResult =
    try {
        code()
        Failure("Expected ${T::class.java.simpleName}, got no exception")
    } catch (e: Throwable) {
        if (e is T) Success
        else Failure("Expected ${T::class.java.simpleName}, got ${e.javaClass.simpleName}")
    }

// Timeout

fun timeout(duration: Duration, code: () -> TestResult): TestResult =
    try {
        CompletableFuture.supplyAsync(code)
            .get(duration.seconds, TimeUnit.SECONDS)
    } catch (e: TimeoutException) {
        Failure("Expected return a value in less than $duration")
    }

// Collection matchers

infix fun <E> Collection<E>.shouldContainOnly(value: E): TestResult =
    if (size == 1 && first() == value) Success
    else Failure("Expected only { $value }, got $this")

infix fun <E> Collection<E>.shouldContainOnly(values: Collection<E>): TestResult =
    if (size == values.size && containsAll(values)) Success
    else Failure("Expected only $values, got $this")

infix fun <E> Collection<E>.shouldHaveSize(size: Int): TestResult =
    if (this.size == size) Success
    else Failure("Expected a size of $size, got ${this.size}")