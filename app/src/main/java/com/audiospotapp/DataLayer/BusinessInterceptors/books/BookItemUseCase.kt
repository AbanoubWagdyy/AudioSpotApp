package com.audiospotapp.DataLayer.BusinessInterceptors.books

import com.audiospot.DataLayer.Model.Book
import com.audiospot.DataLayer.Model.BookDetailsData
import com.audiospotapp.DataLayer.Model.Review

class BookItemUseCase : BookItemInterceptor {

    override fun saveBookReviews(data: List<Review>) {
        this.reviews = data
    }

    override fun getBookReviews(): List<Review> {
        return reviews
    }

    private lateinit var reviews: List<Review>

    var data: Book? = null
    var bookDetails: BookDetailsData? = null

    override fun saveBook(book: Book) {
        this.data = book
    }

    override fun getSavedBook(): Book? {
        return data
    }

    override fun clearSavedBook() {
        data = null
    }

    override fun saveDetails(bookDetails: BookDetailsData) {
        this.bookDetails = bookDetails
    }

    override fun getDetails(): BookDetailsData {
        return this.bookDetails!!
    }
}