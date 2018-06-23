package tests

import models.*
import solver.*
import tests.fwrk.*


val glassSpecs = test("1/ Glass Tests") {

    val (emptyGlass, glass, fullGlass) = listOf("0/10", "5/10", "10/10").map { it.toGlass() }


    describe("1.1/ Glass.isEmpty()") {

        it("return true for an empty Glass") {
            emptyGlass.isEmpty() shouldBe true
        }

        it("return false for a not empty Glass") {
            glass.isEmpty() shouldBe false
        }
    }

    describe("1.2/ Glass.isFull()") {

        it("return true for a full Glass") {
            fullGlass.isFull() shouldBe true
        }

        it("return false for a not full Glass") {
            glass.isFull() shouldBe false
        }
    }

    describe("1.3/ Glass.remainingVolume()") {

        it("return capacity for an empty Glass") {
            emptyGlass.remainingVolume() shouldBe emptyGlass.capacity
        }

        it("return 0 for an fill Glass") {
            fullGlass.remainingVolume() shouldBe 0
        }

        it("return remaining for a normal Glass") {
            glass.remainingVolume() shouldBe (glass.capacity - glass.current)
        }
    }

    describe("1.4/ Glass.empty()") {

        it("return an empty Glass") {
            glass.empty().current shouldBe 0
        }

        it("keep an the Glass capacity") {
            glass.empty().capacity shouldBe glass.capacity
        }
    }

    describe("1.5/ Glass.fill()") {

        it("return an fill Glass") {
            glass.fill().current shouldBe glass.capacity
        }

        it("keep an the Glass capacity") {

            glass.fill().capacity shouldBe glass.capacity
        }
    }

    describe("1.6/ Glass.minus(Int)") {

        it("return glass with subtracted current") {
            glass.minus(2).current shouldBe (glass.current - 2)
        }

        it("keep the Glass capacity") {
            glass.minus(2).capacity shouldBe glass.capacity
        }

        it("keep an the Glass current greater or equal than 0") {
            glass.minus(7).current shouldBe 0
        }

    }

    describe("1.7/ Glass.plus(Int)") {

        it("return glass with added current") {
            glass.plus(2).current shouldBe (glass.current + 2)
        }

        it("keep an the Glass capacity") {
            glass.plus(2).capacity shouldBe glass.capacity
        }

        it("keep an the Glass current lower or equal than capacity") {
            glass.plus(7).current shouldBe glass.capacity
        }

    }
}