package com.niaw.launchnea

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class AbbreviateTest{

    @Test
    fun testAbbreviate() {
        val testCases = mapOf(
            "China Aerospace Science and Technology Corporation" to "CASC",
            "European Space Agency (ESA)" to "ESA",
            "NASA" to "NASA",
            "SpaceX [SX]" to "SX",
            "Small Space Org" to "Small Space Org"  // less than 4 words, stays full
        )

        testCases.forEach { (input, expected) ->
            val result = abbreviate(input)
            assertThat(result).isEqualTo(expected)
        }
    }
}