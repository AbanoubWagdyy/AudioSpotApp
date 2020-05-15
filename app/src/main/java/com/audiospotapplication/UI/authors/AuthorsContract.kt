package com.audiospotapplication.UI.authors

import android.content.Context
import com.audiospotapplication.UI.BaseView
import com.audiospotapplication.DataLayer.Model.AuthorsData
import com.audiospotapplication.DataLayer.Model.AuthorsResponse

interface AuthorsContract {

    interface Presenter {
        fun start()
        fun handleAuthorItemClicked(authorsData: AuthorsData)
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showErrorMessage(message: String)
        fun setAuthorsList(authorsResponse: AuthorsResponse?)
        fun showAuthorDetailsScreen()
        fun showLoading()
        fun dismissLoading()
    }
}