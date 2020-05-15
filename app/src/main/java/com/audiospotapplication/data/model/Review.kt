package com.audiospotapplication.data.model

data class Review(
    val book_id: Int,
    val book_name: String,
    val comment: String,
    val created_at: String,
    val id: Int,
    val name: String,
    val rate: Int
)