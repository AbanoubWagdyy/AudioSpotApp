package com.audiospotapplication.data.model

data class ProfileResponse(
    val APIVersion: String,
    val `data`: ProfileData,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
)