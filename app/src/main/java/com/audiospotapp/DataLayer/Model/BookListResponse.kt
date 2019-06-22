package com.audiospotapp.DataLayer.Model

import com.audiospot.DataLayer.Model.Book

data class BookListResponse(
    val APIVersion: String,
    val `data`: List<Book>,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
)