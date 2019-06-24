package com.audiospotapp.DataLayer.Model

data class PromoCodeResponse(
    val APIVersion: String,
    val `data`: PromoCodeData,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
)