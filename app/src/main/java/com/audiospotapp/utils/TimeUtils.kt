package com.audiospotapp.utils

import com.audiospot.DataLayer.Model.Book
import com.audiospot.DataLayer.Model.BookDetailsData
import com.audiospotapp.DataLayer.Model.ChaptersData

class TimeUtils {

    companion object {

        fun toHours(book: BookDetailsData): String {

            var text = ""

            if (book.total_time != null && !book.total_time.equals("")) {
                text = String.format("%.2f Hours", book.total_time.toFloat() / (1000 * 60 * 60) % 24)
            } else {
                text = "0 Hours"
            }

            return text
        }

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
    }
}