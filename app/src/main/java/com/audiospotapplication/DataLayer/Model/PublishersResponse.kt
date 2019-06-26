package com.audiospotapplication.DataLayer.Model

data class PublishersResponse(
    val APIVersion: String,
    val `data`: List<PublishersResponseData>,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
)