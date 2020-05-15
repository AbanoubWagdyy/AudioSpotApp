package com.audiospotapplication.ui.publishers

import android.content.Context
import com.audiospotapplication.ui.BaseView
import com.audiospotapplication.data.model.PublishersResponse
import com.audiospotapplication.data.model.PublishersResponseData

interface PublishersContract {

    interface Presenter {
        fun start()
        fun handlePublisherItemClicked(data: PublishersResponseData)
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showErrorMessage(message: String)
        fun setPublishersList(result: PublishersResponse?)
        fun showPublishersDetailsScreen()
        fun showLoading()
        fun dismissLoading()
    }
}