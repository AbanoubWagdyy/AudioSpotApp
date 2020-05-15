package com.audiospotapplication.UI.cart

import android.content.Context
import com.audiospot.DataLayer.Model.AuthResponse
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.UI.BaseView

interface CartContract {

    interface Presenter {
        fun start()
        fun deleteBookFromCart(book: Book)
        fun getAuthResponse(): AuthResponse?
        fun saveBook(book: Book)
        fun handlePayNowPressed()
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showErrorMessage()
        fun showLoading()
        fun dismissLoading()
        fun setBookList(listMyBooks: List<Book>)
        fun showMessage(message: String)
        fun showBookDetailsScreen()
        fun setCartCount(size: Int)
        fun showPaymentScreen()
    }
}