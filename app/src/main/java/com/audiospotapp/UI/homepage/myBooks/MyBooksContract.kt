package com.audiospotapp.UI.homepage.myBooks

import android.content.Context
import com.audiospot.DataLayer.Model.Book

interface MyBooksContract {

    interface Presenter {
        fun start()
        fun saveBook(book: Book)
    }

    interface View {
        fun getAppContext(): Context?
        fun showErrorMessage()
        fun showLoading()
        fun dismissLoading()
        fun setBookList(listMyBooks: List<Book>)
        fun showBookDetailsScreen()
        fun showEmptyBooksScreen(strEmptyListBooks: String)
    }
}