package com.audiospotapplication.data.model

data class AuthorsResponse(
    val APIVersion: String,
    val `data`: List<AuthorsData>,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
)