package com.audiospotapplication.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Paragraph(
    val id: Int,
    val title: String,
    val from_time: String,
    val to_time: String,
    val from_time_str: String,
    val to_time_str: String
) : Parcelable