package com.audiospotapplication.UI.authors

import android.content.Context
import com.audiospotapplication.DataLayer.Model.AuthorsData
import com.audiospotapplication.DataLayer.Model.AuthorsResponse

interface AuthorsContract {

    interface Presenter {
        fun start()
        fun handleAuthorItemClicked(authorsData: AuthorsData)
    }

    interface View {
        fun getAppContext(): Context?
        fun showErrorMessage(message: String)
        fun setAuthorsList(authorsResponse: AuthorsResponse?)
        fun showAuthorDetailsScreen()
        fun showLoading()
        fun dismissLoading()
    }
}