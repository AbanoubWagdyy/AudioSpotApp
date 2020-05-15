package com.audiospotapplication.ui.publishers

import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.model.PublishersResponse
import com.audiospotapplication.data.model.PublishersResponseData
import com.audiospotapplication.data.retrofit.RetrofitCallbacks
import com.audiospotapplication.data.retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class PublishersPresenter(val mView: PublishersContract.View) : PublishersContract.Presenter {

    override fun handlePublisherItemClicked(data: PublishersResponseData) {
        mRepositorySource.savePublisherItem(data)
        mView.showPublishersDetailsScreen()
    }

    lateinit var mRepositorySource: RepositorySource

    override fun start() {
        mView.showLoading()
        mRepositorySource = mView.getAppContext()?.let { DataRepository.getInstance(it) }!!
        mRepositorySource.getAllPublishers(object : RetrofitCallbacks.PublishersResponseCallback {
            override fun onSuccess(result: PublishersResponse?) {
                mView.dismissLoading()
                val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                    mView.setPublishersList(result)
                } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                    mView!!.showLoginPage()
                } else {
                    mView!!.showErrorMessage(result!!.message)
                }
            }

            override fun onFailure(call: Call<PublishersResponse>?, t: Throwable?) {
                mView.dismissLoading()
                mView.showErrorMessage("Server Error ,please try again")
            }
        })
    }
}