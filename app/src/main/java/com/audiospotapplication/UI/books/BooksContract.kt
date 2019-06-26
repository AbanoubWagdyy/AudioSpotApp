package com.audiospotapplication.UI.books

import android.content.Context
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.DataLayer.Model.BookListResponse

interface BooksContract {

    interface Presenter {
        fun start()
        fun handleSortBookItemClicked(sortType: String)
        fun handleOnActivityStopped()
        fun saveBook(book: Book)
    }

    interface View {
        fun getAppContext(): Context?
        fun setBooksList(result: BookListResponse?)
        fun showErrorMessage(message: String)
        fun showLoadingDialog()
        fun dismissLoadingDialog()
        fun showBookDetailsScreen()
    }
}