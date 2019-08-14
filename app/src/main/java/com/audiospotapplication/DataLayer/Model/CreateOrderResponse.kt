package com.audiospotapplication.DataLayer.Model

data class CreateOrderResponse(
    val APIVersion: String,
    val `data`: CreateOrderData,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
)