package com.example.calendar.Interface

import com.example.calendar.model.DayMonthYearModel
import java.util.*

interface MonthCalendarInterface {
    fun setDateToView(dmy:DayMonthYearModel)
    fun setCalendarToView(calendar: Calendar)
}