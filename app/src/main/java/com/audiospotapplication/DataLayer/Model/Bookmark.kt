package com.audiospotapplication.DataLayer.Model

data class Bookmark(
    val id: Int,
    val book_id: Int,
    val chapter_id: Int,
    val book_name: String,
    val chapter_name: String,
    val comment: String,
    val time: Int,
    val sound_file: String,
    val book_cover: String
)