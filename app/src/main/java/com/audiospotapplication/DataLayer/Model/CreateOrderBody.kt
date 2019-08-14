package com.audiospotapplication.DataLayer.Model

data class CreateOrderBody(
    val merchant_ref_number: String,
    val promo_code: String,
    val to: String,
    val voucher: String
)