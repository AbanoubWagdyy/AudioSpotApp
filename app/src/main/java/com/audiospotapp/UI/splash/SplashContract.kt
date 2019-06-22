package com.audiospotapp.UI.splash

import androidx.appcompat.app.AppCompatActivity

interface SplashContract {

    interface Presenter {
        fun start()
    }

    interface View {
        fun getActivity(): AppCompatActivity
        fun startHomepageScreen()
        fun startLoginScreen()
    }
}