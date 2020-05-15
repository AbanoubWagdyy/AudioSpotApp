package com.audiospotapplication.UI.updateProfile

import android.content.Context
import com.audiospotapplication.UI.BaseView

interface UpdateProfileContract {

    interface Presenter {
//        fun updateProfile(
//            first_name: String,
//            last_name: String,
//            mobile_phone: String,
//            birthday: String,
//            gender: String,
//            oldPass: String,
//            newPass: String,
//            confirmPass: String
//        )

        fun updateProfile(
            first_name: String,
            last_name: String,
            email: String,
            mobile_phone: String
        )

        fun handleChangePasswordClicked()
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showCompleteYourFieldsMessage()
        fun showInvalidEmailMessage(s: String)
        fun finalizeView()
        fun showErrorMessage(message: String)
        fun setChangePasswordClick()
        fun disableChangePasswordClick()
        fun showLoading()
        fun dismissLoading()
    }
}