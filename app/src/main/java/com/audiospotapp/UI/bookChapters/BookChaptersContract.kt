package com.audiospotapp.UI.bookChapters

import android.content.Context
import com.audiospot.DataLayer.Model.BookDetailsResponse
import com.audiospotapp.DataLayer.Model.ChaptersData
import com.audiospotapp.DataLayer.Model.Review

interface BookChaptersContract {

    interface Presenter {
        fun start()
    }

    interface View {
        fun getAppContext(): Context?
        fun showLoadingDialog()
        fun dismissLoading()
        fun setChapters(data: List<ChaptersData>)
    }
}