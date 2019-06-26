package com.audiospotapplication.utils

import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.DataLayer.Model.ChaptersData
import dm.audiostreamer.MediaMetaData

class BookMediaDataConversion {

    companion object {

        fun convertBookToMediaMetaData(book: Book): MediaMetaData {

            var mediaMetaData = MediaMetaData()
            mediaMetaData.mediaId = book.id.toString()
            mediaMetaData.mediaUrl = book.sample
            mediaMetaData.mediaTitle = book.title
            mediaMetaData.mediaDuration = (book.total_time.toLong() / 1000).toString()

            mediaMetaData.mediaAlbum = ""
            mediaMetaData.mediaArt = book.cover
            mediaMetaData.mediaComposer = ""
            mediaMetaData.mediaArtist = ""

            return mediaMetaData
        }

        fun convertBookToMediaMetaData(book: ChaptersData): MediaMetaData {
            var mediaMetaData = MediaMetaData()
            mediaMetaData.mediaId = book.id.toString()
            mediaMetaData.mediaTitle = book.title
            mediaMetaData.mediaUrl = book.sound_file
            mediaMetaData.mediaDuration = book.duration.toString()

            return mediaMetaData
        }
    }
}