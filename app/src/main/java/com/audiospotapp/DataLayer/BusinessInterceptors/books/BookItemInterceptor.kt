package com.audiospotapp.DataLayer.BusinessInterceptors.books

import com.audiospot.DataLayer.Model.Book
import com.audiospotapp.DataLayer.Model.Review


interface BookItemInterceptor {

    fun saveBook(book: Book)

    fun getSavedBook(): Book?

    fun clearSavedBook()

    fun saveBookReviews(data: List<Review>)

    fun getBookReviews(): List<Review>
}