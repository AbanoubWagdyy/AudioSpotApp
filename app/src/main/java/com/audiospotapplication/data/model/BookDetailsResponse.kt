package com.audiospot.DataLayer.Model

data class BookDetailsResponse(
    val APIVersion: String,
    val data: Book,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
)