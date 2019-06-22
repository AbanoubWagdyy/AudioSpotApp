package com.audiospotapp.DataLayer.Model

data class ChaptersResponse(
    val APIVersion: String,
    val `data`: List<ChaptersData>,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
)