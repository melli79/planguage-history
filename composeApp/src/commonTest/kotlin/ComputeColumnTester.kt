/*
 * Copyright (c) 2024.  Use under Apache Free License 2.0.
 */

import androidx.compose.ui.unit.dp
import kotlin.test.*

class ComputeColumnTester {
    @Test fun beginning() {
        val result = computeColumn(PartialDate(1946,1,1), 100.dp)
        println("1946: $result")
        assertEquals(0.dp, result)
    }

    @Test fun end() {
        val result = computeColumn(PartialDate(2024,7,25), 100.dp)
        println("2024: $result")
        assertEquals(100.dp, result)
    }
}
