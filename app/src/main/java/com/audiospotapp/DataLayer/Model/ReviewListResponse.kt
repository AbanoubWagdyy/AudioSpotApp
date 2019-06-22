package com.audiospotapp.DataLayer.Model

data class ReviewListResponse(
    val APIVersion: String,
    val `data`: List<Review>,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
)