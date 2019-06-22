package com.audiospotapp.UI.giveAgift

import android.content.Context
import com.audiospot.DataLayer.Model.BookDetailsData
import com.audiospot.DataLayer.Model.BookDetailsResponse
import com.audiospotapp.DataLayer.Model.Review

interface GiveGiftContract {

    interface Presenter {
        fun start()
        fun giveGift(email: String)
    }

    interface View {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun bindResponse(result: BookDetailsData?)
        fun showInvalidEmailMessage(message: String)
        fun showMessage(message: String)
        fun finalizeView()
    }
}