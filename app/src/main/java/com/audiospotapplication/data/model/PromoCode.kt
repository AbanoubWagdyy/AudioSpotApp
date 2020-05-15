package com.audiospotapplication.data.model

data class PromoCode(
    val author_id: Int,
    val book_id: Any,
    val deleted_at: Any,
    val deleted_by: Any,
    val expire_date: Any,
    val id: Int,
    val is_active: Int,
    val narrator_id: Any,
    val percentage: Int,
    val promo_code: String,
    val promo_user_limit: Int,
    val publisher_id: Any,
    val type: Any,
    val updated_at: String,
    val updated_by: Int
)