package com.audiospotapplication.ui.authorDetails

import android.content.Context
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.ui.BaseView
import com.audiospotapplication.data.model.BookListResponse

interface AuthorDetailsContract {

    interface Presenter {
        fun start()
        fun saveBook(book: Book)
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showLoading()
        fun dismissLoading()
        fun setAuthorBio(bio: String)
        fun setAuthorName(name: String)
        fun setAuthorImage(photo: String)
        fun setBookList(result: BookListResponse?)
        fun showErrorMessage(message: String)
        fun showBookDetailsScreen()
    }
}