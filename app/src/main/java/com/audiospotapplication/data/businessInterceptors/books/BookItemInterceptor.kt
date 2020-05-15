package com.audiospotapplication.data.businessInterceptors.books

import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.data.model.ChaptersData
import com.audiospotapplication.data.model.Review


interface BookItemInterceptor {

    fun saveBook(book: Book?)

    fun getSavedBook(): Book?

    fun clearSavedBook()

    fun saveBookReviews(data: List<Review>?)

    fun getBookReviews(): List<Review>?

    fun saveBookChapters(mediaItems: List<ChaptersData>?)

    fun getBookChapters(): List<ChaptersData>?
}