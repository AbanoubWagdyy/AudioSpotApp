package com.audiospotapplication.DataLayer.BusinessInterceptors.books

import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.DataLayer.Model.ChaptersData
import com.audiospotapplication.DataLayer.Model.Review

class BookItemUseCase : BookItemInterceptor {
    override fun saveBookChapters(mediaItems: List<ChaptersData>) {
        this.chapters = mediaItems
    }

    override fun getBookChapters(): List<ChaptersData> {
        return chapters
    }

    override fun saveBookReviews(data: List<Review>) {
        this.reviews = data
    }

    override fun getBookReviews(): List<Review> {
        return reviews
    }

    private lateinit var reviews: List<Review>
    private lateinit var chapters: List<ChaptersData>

    var data: Book? = null

    override fun saveBook(book: Book?) {
        this.data = book
    }

    override fun getSavedBook(): Book? {
        return data
    }

    override fun clearSavedBook() {
        data = null
    }
}