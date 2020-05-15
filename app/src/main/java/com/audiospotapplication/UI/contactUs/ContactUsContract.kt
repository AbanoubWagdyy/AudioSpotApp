package com.audiospotapplication.UI.contactUs

import android.content.Context
import com.audiospotapplication.UI.BaseView

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