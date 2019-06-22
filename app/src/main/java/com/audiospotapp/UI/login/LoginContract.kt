package com.audiospotapp.UI.login

import androidx.appcompat.app.AppCompatActivity
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapp.utils.DialogUtils

//import com.github.windsekirun.rxsociallogin.intenal.model.LoginResultItem

interface LoginContract {

    interface Presenter {
        fun start()
        fun destroyView()
        fun login(username: String, password: String)
        fun validateForgetPasswordClicked()
        fun validateSignUpClicked()
        fun validateSkipClicked()
        fun handleSocialLoginPressed(
            firstName: String, lastName: String, email: String, platform: String, id: String)
    }

    interface View {
        fun getActivity(): AppCompatActivity
        fun viewRegisterScreen()
        fun viewHomepageScreen()
        fun viewForgetPasswordScreen()
        fun viewIncorrectDataMessage()
        fun showErrorMessage(message: String)
        fun showProgressDialog()
        fun hideProgressDialog()
    }
}