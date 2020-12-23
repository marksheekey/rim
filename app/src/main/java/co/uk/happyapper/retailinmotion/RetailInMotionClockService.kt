package co.uk.happyapper.retailinmotion

import java.util.*
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.MINUTE

class RetailInMotionClockService : ClockService {
    override fun getNow() = Calendar.getInstance().timeInMillis
    override fun getTimeFromMillis(millis: Long): Int{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        val hours = calendar.get(HOUR_OF_DAY)
        val minutes = calendar.get(MINUTE)
        return (hours * 100) + minutes
    }
}

interface ClockService {
    fun getNow(): Long
    fun getTimeFromMillis(millis: Long): Int
}