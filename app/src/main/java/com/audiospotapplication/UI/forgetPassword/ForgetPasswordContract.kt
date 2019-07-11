package com.audiospotapplication.UI.forgetPassword

import android.app.Activity

interface ForgetPasswordContract {

    interface Presenter {
        fun onCancelPressed()
        fun onResetPasswordClicked(email: String)
    }

    interface View {
        fun getActivity(): Activity
        fun finishScreen()
        fun viewLoginScreen()
        fun showErrorMessage(message: String)
        fun showInvalidEmailAddressMessage(message: String)
        fun showProgressDialog()
        fun hideProgressDialog()
    }
}