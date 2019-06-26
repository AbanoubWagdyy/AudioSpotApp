package com.audiospotapplication.DataLayer.Model

data class ProfileResponse(
    val APIVersion: String,
    val `data`: ProfileData,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
)