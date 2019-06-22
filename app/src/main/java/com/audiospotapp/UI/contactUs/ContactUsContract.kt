package com.audiospotapp.UI.contactUs

import android.content.Context
import com.audiospot.DataLayer.Model.BookDetailsData
import com.audiospot.DataLayer.Model.BookDetailsResponse
import com.audiospotapp.DataLayer.Model.Review

interface ContactUsContract {

    interface Presenter {
        fun start()
        fun contactUs(email: String, subject: String, message: String)
    }

    interface View {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun finalizeView()
    }
}