package com.audiospotapplication.ui.authors

import android.content.Context
import com.audiospotapplication.ui.BaseView
import com.audiospotapplication.data.model.AuthorsData
import com.audiospotapplication.data.model.AuthorsResponse

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