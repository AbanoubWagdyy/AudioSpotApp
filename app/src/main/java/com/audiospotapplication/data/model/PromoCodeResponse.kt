package com.audiospotapplication.data.model

data class PromoCodeResponse(
    val APIVersion: String,
    val `data`: Data,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
)