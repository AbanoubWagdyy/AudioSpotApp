package com.audiospotapp.UI.rateBook

import android.content.Context
import com.audiospot.DataLayer.Model.Book

interface RateBookContract {

    interface Presenter {
        fun start()
        fun rateBook(rating: Float, message: String)
    }

    interface View {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun bindResponse(bookDetailsData: Book)
        fun showMessage(message: String)
    }
}