package com.audiospotapplication.ui.giveAgift

import android.content.Context
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.ui.BaseView

interface GiveGiftContract {

    interface Presenter {
        fun start()
        fun giveGift(
            email1: String,
            email2: String,
            email3: String,
            email4: String,
            email5: String
        )
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun bindResponse(result: Book?, quantity: Int)
        fun showInvalidEmailMessage(message: String)
        fun showMessage(message: String)
        fun finalizeView()
        fun showPayment(emails: String, voucher: String, id: Int)
    }
}