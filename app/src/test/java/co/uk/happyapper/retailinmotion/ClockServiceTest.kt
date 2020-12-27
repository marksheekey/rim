package co.uk.happyapper.retailinmotion

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import org.junit.Test

class ClockServiceTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun testTimeIs12() {
        val service = RetailInMotionClockService()
        val time = service.getTimeFromMillis(1577880000000)
        assert(time == 1200)
    }
}