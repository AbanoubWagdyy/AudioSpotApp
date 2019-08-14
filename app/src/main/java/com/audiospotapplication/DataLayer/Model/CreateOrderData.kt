package com.audiospotapplication.DataLayer.Model

data class CreateOrderData(
    val created_at: String,
    val id: Int,
    val merchant_ref_number: String,
    val order_type: String,
    val promo_code: String,
    val status: String,
    val to: String,
    val updated_at: String,
    val user_id: Int,
    val voucher: String
)