package com.octopus.edu.kotlin.spacex.core.utils

import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTimeUtils {
    fun convertDate(
        date: String,
        initialFormat: DateFormat = DateFormat.ISO_8601,
        finalFormat: DateFormat = DateFormat.DATE_AND_TIME_US,
    ): String? =
        runCatching {
            val initialFormatter = DateTimeFormatter.ofPattern(initialFormat.format, Locale.getDefault())
            val finalFormatter = DateTimeFormatter.ofPattern(finalFormat.format, Locale.getDefault())
            return if (initialFormat == DateFormat.ISO_8601) {
                ZonedDateTime.parse(date).format(finalFormatter)
            } else {
                LocalDate.parse(date, initialFormatter).format(finalFormatter)
            }
        }.getOrNull()

    enum class DateFormat(val format: String) {
        ISO_8601("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
        DATE_AND_TIME_US("MM/dd/yyyy hh:mm:ss"),
        DATE_AND_TIME_EU("dd/MM/yyyy hh:mm:ss"),
        SHORT_DATE_US("MM/dd/yyyy"),
        SHORT_DATE_EU("dd/MM/yyyy"),
        LONG_DATE_US("MMMM d, yyyy"),
        LONG_DATE_UK("d MMMM yyyy"),
        LONG_DATE_AND_TIME_US("d MMMM yyyy hh:mm:ss"),
        LONG_DATE_AND_TIME_UK("d MMMM yyyy hh:mm:ss"),
        TIME_ONLY_12_HOUR("hh:mm a"),
        TIME_ONLY_24_HOUR("hh:mm a"),
        COMPACT("yyyyMMdd_HHmmss"),
    }
}
