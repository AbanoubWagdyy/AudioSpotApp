package com.audiospotapplication.data.model

data class CreateOrderBody(
    val merchant_ref_number: String,
    val promo_code: String,
    val to: String,
    val voucher: String
)