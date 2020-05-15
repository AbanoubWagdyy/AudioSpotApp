package com.audiospotapplication.data.model

data class ChaptersResponse(
    val APIVersion: String,
    val `data`: List<ChaptersData>,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
)