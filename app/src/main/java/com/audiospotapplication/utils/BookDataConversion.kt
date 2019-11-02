package com.audiospotapplication.utils

import com.audiospot.DataLayer.Model.Book
import com.example.jean.jcplayer.model.JcAudio
import dm.audiostreamer.MediaMetaData

class BookDataConversion {

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

        fun convertBookToJcAudio(book: Book): JcAudio {
            return JcAudio.createFromURL(book.id, book.title, book.sample, null)
        }
    }
}