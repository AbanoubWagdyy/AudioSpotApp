package com.audiospotapplication.DataLayer.BusinessInterceptors.books

import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.DataLayer.Model.ChaptersData
import com.audiospotapplication.DataLayer.Model.Review


interface BookItemInterceptor {

    fun saveBook(book: Book?)

    fun getSavedBook(): Book?

    fun clearSavedBook()

    fun saveBookReviews(data: List<Review>?)

    fun getBookReviews(): List<Review>?

    fun saveBookChapters(mediaItems: List<ChaptersData>?)

    fun getBookChapters(): List<ChaptersData>?
}