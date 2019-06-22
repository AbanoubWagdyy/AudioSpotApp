package com.audiospotapp.UI.changePassword

import android.content.Context

interface ChangePasswordContract {

    interface Presenter {
        fun updatePassword(
            old_password: String,
            new_password: String,
            confirm_password: String
        )
    }

    interface View {
        fun getAppContext(): Context?
        fun showIncorrectOldPassword(s: String)
        fun showCompleteAllFieldsMessage(s: String)
        fun finalizeView()
        fun showErrorMessage(message: String)
        fun showLoading()
        fun dismissLoading()
    }
}