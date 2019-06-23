package com.audiospot.DataLayer.Model

import com.audiospotapp.DataLayer.Model.Narrator

data class Book(
    val about_book: String,
    val author: String,
    val category: String,
    val cover: String,
    val id: Int,
    val is_buy: Int,
    val is_cart: Int,
    val is_favorite: Int,
    val language: String,
    val narators: List<Narrator>,
    val price: Int,
    val price_after_sale: Int,
    val price_type: Int,
    val publish_date: String,
    val publisher: String,
    val rate: Double,
    val release_date: String,
    val reviews: Int,
    val sample: String,
    val title: String,
    val total_time: String,
    val total_time_trt: String,
    var isToShowDelete: Boolean = false
)