package com.audiospotapplication.data.model

data class Response(
    val APIVersion: String,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
)