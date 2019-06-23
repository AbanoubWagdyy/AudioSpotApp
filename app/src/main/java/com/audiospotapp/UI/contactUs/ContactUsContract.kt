package com.audiospotapp.UI.contactUs

import android.content.Context

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