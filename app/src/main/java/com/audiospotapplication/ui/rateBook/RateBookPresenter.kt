package com.audiospotapplication.ui.rateBook

import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.model.Response
import com.audiospotapplication.data.retrofit.RetrofitCallbacks
import com.audiospotapplication.data.retrofit.RetrofitResponseHandler
import com.audiospotapplication.data.RepositorySource
import retrofit2.Call

class RateBookPresenter(val mView: RateBookContract.View) : RateBookContract.Presenter {

    override fun rateBook(rating: Float, message: String) {
        mView.showLoadingDialog()
        mRepositorySource?.rateBook(rating.toInt(), message, object : RetrofitCallbacks.ResponseCallback {
            override fun onSuccess(result: Response?) {
                mView.dismissLoading()
                if (result != null)
                    mView.showMessage(result.message)
                val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                    mView.showHompageScreen()
                } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                    mView!!.showLoginPage()
                }
            }

            override fun onFailure(call: Call<Response>?, t: Throwable?) {
                mView.dismissLoading()
                mView.showMessage("Please Check your internet connection")
            }

            override fun onAuthFailure() {
                mView.dismissLoading()
            }
        })
    }

    override fun start() {
        mRepositorySource = mView.getAppContext()?.let { DataRepository.getInstance(it) }
        var bookDetailsData = mRepositorySource?.getSavedBook()
        mView.bindResponse(bookDetailsData!!)
    }

    var mRepositorySource: RepositorySource? = null
    lateinit var bookDetails: Book
}