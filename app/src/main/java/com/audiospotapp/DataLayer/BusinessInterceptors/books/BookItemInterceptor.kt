package com.audiospotapp.DataLayer.BusinessInterceptors.books

import com.audiospot.DataLayer.Model.Book
import com.audiospot.DataLayer.Model.BookDetailsData
import com.audiospotapp.DataLayer.Model.Review


interface BookItemInterceptor {

    fun saveBook(book: Book)

    fun getSavedBook(): Book?

    fun clearSavedBook()

    fun saveDetails(bookDetails: BookDetailsData)

    fun getDetails(): BookDetailsData

    fun saveBookReviews(data: List<Review>)

    fun getBookReviews(): List<Review>
}