package com.audiospotapp.DataLayer.Model

data class Bookmark(
    val book_id: Int,
    val chapter_id: Int,
    val comment: String,
    val id: Int,
    val sound_file: String,
    val time: Int
)