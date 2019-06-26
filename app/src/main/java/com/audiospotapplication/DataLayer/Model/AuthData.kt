package com.audiospot.DataLayer.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AuthData(
    val app_version_android: String,
    val app_version_ios: String,
    var email: String,
    val first_name: String,
    val full_name: String,
    val last_name: String,
    val phone: String,
    var display_name: String = "",
    var profile_photo: String = "",
    val token: String,
    var Password: String = ""
) : Parcelable