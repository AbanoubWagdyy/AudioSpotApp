package com.audiospotapp.UI.publishers

import android.content.Context
import com.audiospotapp.DataLayer.Model.AuthorsResponse
import com.audiospotapp.DataLayer.Model.BookListResponse
import com.audiospotapp.DataLayer.Model.PublishersResponse
import com.audiospotapp.DataLayer.Model.PublishersResponseData

interface PublishersContract {

    interface Presenter {
        fun start()
        fun handlePublisherItemClicked(data: PublishersResponseData)
    }

    interface View {
        fun getAppContext(): Context?
        fun showErrorMessage(message: String)
        fun setPublishersList(result: PublishersResponse?)
        fun showPublishersDetailsScreen()
        fun showLoading()
        fun dismissLoading()
    }
}