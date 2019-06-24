package com.audiospotapp.UI.giveAgift

import android.content.Context
import com.audiospot.DataLayer.Model.Book

interface GiveGiftContract {

    interface Presenter {
        fun start()
        fun giveGift(
            email1: String,
            email2: String,
            email3: String,
            email4: String,
            email5: String)
    }

    interface View {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun bindResponse(result: Book?)
        fun showInvalidEmailMessage(message: String)
        fun showMessage(message: String)
        fun finalizeView()
        fun showCartScreen()
    }
}