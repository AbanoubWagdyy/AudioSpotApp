package com.audiospotapplication.ui.search

import androidx.appcompat.app.AppCompatActivity

interface SearchContract {

    interface Presenter {
        fun start()
    }

    interface View {
        fun getActivity(): AppCompatActivity
    }
}