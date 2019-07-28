package com.audiospotapplication.UI.giftSelection

import android.content.Context
import com.audiospot.DataLayer.Model.Book

interface GiftSelectionContract {

    interface Presenter {
        fun start()
        fun handleSubmitClicked(giftSelection: GiftSelection, quantity: Int)
    }

    interface View {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun bindResponse(bookDetailsData: Book)
        fun showMessage(message: String)
        fun showGiftSCreen()
        fun showCartScreen()
        fun showPayment(id: Int, quantity: Int)
    }
}