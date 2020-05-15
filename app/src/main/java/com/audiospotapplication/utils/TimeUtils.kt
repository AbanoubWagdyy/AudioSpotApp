package com.audiospotapplication.utils

import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.DataLayer.Model.ChaptersData
import java.text.SimpleDateFormat
import java.util.*


class TimeUtils {

    companion object {

        fun toHours(book: Book): String {
            var text = ""

            if (book.total_time != null && !book.total_time.equals("")) {
                text = String.format("%.2f Hours", book.total_time.toFloat() / (1000 * 60 * 60) % 24)
            } else {
                text = "0 Hours"
            }

            return text
        }

        fun toHours(chapterData: ChaptersData): String {
            var text = ""

            if (chapterData.duration != null && !chapterData.duration.equals("")) {
                text = String.format("%.2f Hours", chapterData.duration.toFloat() / (1000 * 60 * 60) % 24)
            } else {
                text = "0 Hours"
            }
            return text
        }

        fun toTimeFormat(seconds: Int): String {
            val d = Date(seconds * 1000L)
            val df = SimpleDateFormat("HH:mm:ss") // HH for 0-23
            df.setTimeZone(TimeZone.getTimeZone("GMT"))
            val timeString = df.format(d)
            return timeString
        }
    }
}