package com.audiospotapp.UI.giveAgift

import android.content.Context
import com.audiospot.DataLayer.Model.Book

interface GiveGiftContract {

    interface Presenter {
        fun start()
        fun giveGift(email: String)
    }

    interface View {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun bindResponse(result: Book?)
        fun showInvalidEmailMessage(message: String)
        fun showMessage(message: String)
        fun finalizeView()
    }
}