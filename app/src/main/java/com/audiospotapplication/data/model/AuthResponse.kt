package com.audiospot.DataLayer.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AuthResponse(
    val APIVersion: String,
    val `data`: AuthData,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
) : Parcelable