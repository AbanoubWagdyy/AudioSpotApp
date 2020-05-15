package com.audiospotapplication.UI.giftSelection

import android.content.Context
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.BaseView

interface GiftSelectionContract {

    interface Presenter {
        fun start()
        fun handleSubmitClicked(giftSelection: GiftSelection, quantity: Int)
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun bindResponse(bookDetailsData: Book)
        fun showMessage(message: String)
        fun showGiftSCreen()
        fun showPayment(id: Int, quantity: Int)
    }
}