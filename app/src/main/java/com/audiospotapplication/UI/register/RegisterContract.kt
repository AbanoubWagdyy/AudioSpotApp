package com.audiospotapplication.UI.register

import androidx.appcompat.app.AppCompatActivity


interface RegisterContract {

    interface Presenter {
        fun register(
            first_name: String,
            last_name: String,
            email: String,
            mobile_phone: String,
            password: String,
            confirm_password: String
        )
    }

    interface View{
        fun getActivity(): AppCompatActivity
        fun viewHomepageScreen()
        fun viewIncorrectDataMessage()
        fun showErrorMessage(message: String)
        fun showProgressDialog()
        fun hideProgressDialog()
    }
}