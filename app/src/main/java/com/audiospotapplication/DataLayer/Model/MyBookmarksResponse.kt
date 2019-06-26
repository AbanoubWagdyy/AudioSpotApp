package com.audiospotapplication.DataLayer.Model

data class MyBookmarksResponse(
    val APIVersion: String,
    val `data`: List<Bookmark>,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
)