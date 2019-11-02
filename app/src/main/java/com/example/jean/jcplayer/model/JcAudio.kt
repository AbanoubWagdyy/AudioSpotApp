package com.example.jean.jcplayer.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RawRes
import com.example.jean.jcplayer.Paragraph
import com.example.jean.jcplayer.general.Origin
import kotlinx.android.parcel.Parcelize


/**
 * This class is an type of audio model .
 * @author Jean Carlos (Github: @jeancsanchez)
 * @date 27/06/16.
 * Jesus loves you.
 */

@Parcelize
data class JcAudio constructor(
    var id: Int,
    var title: String,
    var position: Int? = -1,
    val path: String,
    val origin: Origin,
    var paragraphs: List<Paragraph>?
) : Parcelable {

//    constructor(source: Parcel) : this(
//        source.readString()!!,
//        source.readValue(Int::class.java.classLoader) as Int?,
//        source.readString()!!,
//        Origin.valueOf(source.readString()!!)
//    )
//
//    override fun describeContents() = 0
//
//    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
//        writeString(title)
//        writeValue(position)
//        writeString(path)
//        writeString(origin.name)
//        writeList(paragraphs)
//    }

    companion object {

        @JvmStatic
        fun createFromURL(id: Int, title: String, url: String, paragraphs: List<Paragraph>?): JcAudio {
            return JcAudio(id = id, title = title, path = url, origin = Origin.URL, paragraphs = paragraphs)
        }
//        @JvmStatic
//        fun createFromFilePath(id: Int, filePath: String, paragraphs: List<Paragraph>): JcAudio {
//            return JcAudio(
//                id = id,
//                title = filePath,
//                path = filePath,
//                origin = Origin.FILE_PATH,
//                paragraphs = paragraphs
//            )
//        }
        @JvmStatic
        fun createFromFilePath(id: Int, title: String, filePath: String, paragraphs: List<Paragraph>): JcAudio {
            return JcAudio(id = id, title = title, path = filePath, origin = Origin.FILE_PATH, paragraphs = paragraphs)
        }
    }
}