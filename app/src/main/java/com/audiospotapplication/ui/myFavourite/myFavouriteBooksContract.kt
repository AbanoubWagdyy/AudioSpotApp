package com.audiospotapplication.ui.myFavourite

import android.content.Context
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.ui.BaseView

interface myFavouriteBooksContract {

    interface Presenter {
        fun start()
        fun saveBook(book: Book)
        fun removeFromFavorites(book: Book)
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showErrorMessage()
        fun showLoading()
        fun dismissLoading()
        fun setBookList(listMyBooks: List<Book>)
        fun showBookDetailsScreen()
        fun showMessage(message: String)
    }
}