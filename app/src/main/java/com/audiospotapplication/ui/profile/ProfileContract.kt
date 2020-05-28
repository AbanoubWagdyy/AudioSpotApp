package com.audiospotapplication.ui.profile

import android.content.Context
import com.audiospotapplication.ui.BaseView

interface ProfileContract {

    interface Presenter {
        fun start()
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun setFullName(full_name: String?)
        fun setEmail(email: String?)
        fun setMobilePhone(phone: String?)
        fun showErrorMessage(s: String?)
        fun setUserImage(profile_photo: String?)
    }
}