package com.audiospotapplication.ui.publisherDetails

import android.content.Context
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.ui.BaseView
import com.audiospotapplication.data.model.BookListResponse

interface PublisherDetailsContract {

    interface Presenter {
        fun start()
        fun saveBook(book: Book)
    }

    interface View : BaseView {
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