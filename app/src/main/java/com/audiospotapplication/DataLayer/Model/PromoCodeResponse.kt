package com.audiospotapplication.DataLayer.Model

data class PromoCodeResponse(
    val APIVersion: String,
    val `data`: Data,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
)