package tests


import models.*
import solver.*
import tests.fwrk.*
import java.time.Duration


val solveSpecs = test("2/ Solver Tests") {

    describe("2.1/ findSolution()") {

        it("return null for an empty history") {
            val expectedState = "0/2, 0/1".toState()
            val statesWithHistory = emptyList<StateWithHistory>()

            val moves = findSolution(statesWithHistory, expectedState)

            moves shouldBe null
        }

        it("return null for an history without the state") {
            val expectedState = "0/2, 0/1".toState()
            val statesWithHistory = listOf(
                "0/3, 0/1".toState() to listOf(Empty(1)),
                "0/4, 0/1".toState() to listOf(Empty(1)),
                "0/5, 0/1".toState() to listOf(Empty(1)))

            val moves = findSolution(statesWithHistory, expectedState)

            moves shouldBe null
        }

        it("return list of move for an history with the state") {
            val expectedState = "0/2, 0/1".toState()
            val expectedSolution = listOf(Fill(1))
            val statesWithHistory = listOf(
                "0/3, 0/1".toState() to listOf(Empty(1)),
                "0/4, 0/1".toState() to listOf(Empty(1)),
                "0/5, 0/1".toState() to listOf(Empty(1)),
                expectedState to expectedSolution)

            val moves = findSolution(statesWithHistory, expectedState)

            moves shouldBe expectedSolution
        }
    }

    describe("2.2/ State.availableMoves()") {

        it("return an empty collection if state contains no Glass") {
            val state = emptyList<Glass>()

            val moves = state.availableMoves()

            moves shouldBe emptyList()
        }

        it("return an Empty if state contains one filled Glass") {
            val state = "10/10".toState()

            val moves = state.availableMoves()


            moves shouldContainOnly Empty(0)
        }

        it("return an Fill if state contains one filled Glass") {
            val state = "0/10".toState()

            val moves = state.availableMoves()

            moves shouldContainOnly Fill(0)
        }

        it("return an Fill and an Empty if state contains one Glass neither empty nor fill") {
            val state = "4/10".toState()

            val moves = state.availableMoves()

            moves shouldContainOnly listOf(Empty(0), Fill(0))
        }

        it("return six moves for 2 Glasses neither empty nor fill") {
            val state = "5/10, 1/5".toState()

            val moves = state.availableMoves()

            moves shouldContainOnly listOf(Empty(0), Fill(0),
                                           Empty(1), Fill(1),
                                           Pour(0, 1), Pour(1, 0))
        }
    }

    describe("2.3/ State.process()") {
        val initialState = "4/5, 1/3, 0/2".toState()

        it("should process Empty") {

            val state = initialState.process(Empty(0))

            val expected = "0/5, 1/3, 0/2".toState()
            state shouldBe expected
        }

        it("should process Fill") {

            val state = initialState.process(Fill(1))

            val expected = "4/5, 3/3, 0/2".toState()
            state shouldBe expected
        }

        it("should process Pour") {

            val state = initialState.process(Pour(from = 0, to = 2))

            val expected = "2/5, 1/3, 2/2".toState()
            state shouldBe expected
        }
    }

    describe("2.4/ nextStatesFromState()") {

        it("return list of new state with history") {
            val state = "5/10, 1/5".toState()

            val next = nextStatesFromState(state to emptyList())

            next shouldContainOnly listOf(
                "0/10, 1/5".toState() to listOf(Empty(0)),
                "10/10, 1/5".toState() to listOf(Fill(0)),
                "5/10, 0/5".toState() to listOf(Empty(1)),
                "5/10, 5/5".toState() to listOf(Fill(1)),
                "1/10, 5/5".toState() to listOf(Pour(0, 1)),
                "6/10, 0/5".toState() to listOf(Pour(1, 0)))
        }

    }

    describe("2.5/ nextStatesFromCollection()") {

        it("return list of new state with history") {
            val states = listOf<StateWithHistory>(
                "10/10".toState() to emptyList(),
                "0/10".toState() to emptyList())

            val next = nextStatesFromCollection(states)

            next shouldContainOnly listOf(
                "0/10".toState() to listOf(Empty(0)),
                "10/10".toState() to listOf(Fill(0)))
        }

    }

    describe("2.6/ allVisitedStates()") {

        it("return list of new state with history") {
            val visitedStates = setOf(
                "2/10".toState(),
                "5/10".toState(),
                "10/10".toState())
            val newStates = listOf(
                "0/10".toState() to listOf(Empty(0)),
                "10/10".toState() to listOf(Fill(0)))

            val states = allVisitedStates(visitedStates, newStates)

            states shouldContainOnly listOf(
                "0/10".toState(),
                "2/10".toState(),
                "5/10".toState(),
                "10/10".toState())
        }
    }

    describe("2.7/ solve()") {

        it("should solve 0/5, 0/3 to 4/5, 0/3") {
            val from = "0/5, 0/3".toState()
            val to = "4/5, 0/3".toState()

            val solution = solve(from, to)

            solution shouldHaveSize 7
        }

        it("should solve 0/8, 0/5 to 6/8, 0/5") {
            val from = "0/8, 0/5".toState()
            val to = "6/8, 0/5".toState()

            val solution = solve(from, to)

            solution shouldHaveSize 7
        }

        it("should fail if there is no solution") {
            val from = "0/8, 0/4, 0/2".toState()
            val to = "1/8, 0/4, 0/2".toState()

            timeout(Duration.ofSeconds(1)) {
                shouldThrow<IllegalStateException> {
                    solver.solve(from, to)
                }
            }
        }
    }
}