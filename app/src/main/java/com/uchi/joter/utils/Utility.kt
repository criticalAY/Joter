package com.uchi.joter.utils

import java.text.SimpleDateFormat
import java.util.*

object Utility {

    const val NOTE_TITLE = "note_title"
    const val NOTE_CONTENT = "note_content"
    const val NOTE_DATE = "note_date"
    const val NOTE_ID = "note_id"

    /** Returns time in format hour:minute AM/PM as a string **/
    fun getFormattedTime(): String {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return timeFormat.format(Date())
    }

    /** Returns date in format [day/month/year] as a string **/
    fun getFormattedDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

}