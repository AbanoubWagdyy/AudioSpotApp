package com.audiospotapplication.ui.contactUs

import android.content.Context
import com.audiospotapplication.ui.BaseView

interface ContactUsContract {

    interface Presenter {
        fun start()
        fun contactUs(message: String)
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun finalizeView()
        fun showMessage(message: String)
        fun showHompageScreen()
    }
}