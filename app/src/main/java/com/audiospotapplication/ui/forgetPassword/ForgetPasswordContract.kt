package com.audiospotapplication.ui.forgetPassword

import android.app.Activity
import com.audiospotapplication.ui.BaseView

interface ForgetPasswordContract {

    interface Presenter {
        fun onCancelPressed()
        fun onResetPasswordClicked(email: String)
        fun resetRepo()
    }

    interface View : BaseView {
        fun getActivity(): Activity
        fun finishScreen()
        fun viewLoginScreen()
        fun showErrorMessage(message: String)
        fun showInvalidEmailAddressMessage(message: String)
        fun showProgressDialog()
        fun hideProgressDialog()
    }
}