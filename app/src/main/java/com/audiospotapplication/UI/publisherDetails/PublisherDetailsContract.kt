package com.audiospotapplication.UI.publisherDetails

import android.content.Context
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.DataLayer.Model.BookListResponse

interface PublisherDetailsContract {

    interface Presenter {
        fun start()
        fun saveBook(book: Book)
    }

    interface View {
        fun getAppContext(): Context?
        fun showLoading()
        fun dismissLoading()
        fun setPublisherBio(bio: String)
        fun setPublisherName(name: String)
        fun setPublisherImage(photo: String)
        fun setBookList(result: BookListResponse?)
        fun showErrorMessage(message: String)
        fun showBookDetailsScreen()
    }
}