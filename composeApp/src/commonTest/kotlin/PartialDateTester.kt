/*
 * Copyright (c) 2024.  Use under Apache Free License 2.0.
 */

import kotlin.test.*

class PartialDateTester {
    @Test fun sorting() {
        assertTrue(PartialDate(1948) <= PartialDate(1948))
        assertFalse(PartialDate(1948) < PartialDate(1948))
        assertEquals(PartialDate(1948).compareTo(PartialDate(1948, 1, 1)), 0)
        assertNotEquals(PartialDate(1948,1), PartialDate(1948, 2))
    }

    @Test fun toDays() {
        for ((i, e) in listOf(Pair(PartialDate(1946,1,1), 0),
                Pair(PartialDate(1947,1,1), 365),
                Pair(PartialDate(1946,2,1), 30))) {
            val result = i.toDays()
            println("$i is $result")
            assertEquals(e, result)
        }
        println("2024-7-24 is ${PartialDate(2024,7,25).toDays()}")
    }
}
