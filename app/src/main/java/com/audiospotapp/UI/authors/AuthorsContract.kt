package com.audiospotapp.UI.authors

import android.content.Context
import com.audiospotapp.DataLayer.Model.AuthorsData
import com.audiospotapp.DataLayer.Model.AuthorsResponse
import com.audiospotapp.DataLayer.Model.BookListResponse

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