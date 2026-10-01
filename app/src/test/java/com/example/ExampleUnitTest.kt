package com.example

import com.example.ui.DELIVERY_LOCATIONS
import com.example.ui.components.formatUgx
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testCurrencyFormatting() {
        val formatted = formatUgx(15000)
        assertEquals("UGX 15,000", formatted)
    }

    @Test
    fun testDeliveryLocations() {
        assertTrue(DELIVERY_LOCATIONS.containsKey("Kampala"))
        assertEquals(5000L, DELIVERY_LOCATIONS["Kampala"])
        assertEquals(0L, DELIVERY_LOCATIONS["Shop pickup (Kampala)"])
    }
}
