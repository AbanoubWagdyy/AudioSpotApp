package com.audiospotapplication.UI.publishers

import android.content.Context
import com.audiospotapplication.DataLayer.Model.PublishersResponse
import com.audiospotapplication.DataLayer.Model.PublishersResponseData

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