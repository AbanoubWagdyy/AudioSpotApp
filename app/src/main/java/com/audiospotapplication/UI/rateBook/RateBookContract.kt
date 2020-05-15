package com.audiospotapplication.UI.rateBook

import android.content.Context
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.UI.BaseView

interface RateBookContract {

    interface Presenter {
        fun start()
        fun rateBook(rating: Float, message: String)
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun bindResponse(bookDetailsData: Book)
        fun showMessage(message: String)
        fun showHompageScreen()
    }
}