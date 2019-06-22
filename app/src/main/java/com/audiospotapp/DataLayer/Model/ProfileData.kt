package com.audiospotapp.DataLayer.Model

data class ProfileData(
    val app_version_android: String,
    val app_version_ios: String,
    val display_name: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val profile_photo: String
)