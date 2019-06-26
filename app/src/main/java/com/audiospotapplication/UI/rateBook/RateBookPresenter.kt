package com.audiospotapplication.UI.rateBook

import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Model.Response
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.DataLayer.Retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class RateBookPresenter(val mView: RateBookContract.View) : RateBookContract.Presenter {

    override fun rateBook(rating: Float, message: String) {
        mView.showLoadingDialog()
        mRepositorySource.rateBook(rating.toInt(), message, object : RetrofitCallbacks.ResponseCallback {
            override fun onSuccess(result: Response?) {
                mView.dismissLoading()
                if (result != null)
                    mView.showMessage(result.message)
                val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                    mView.showHompageScreen()
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
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
        var bookDetailsData = mRepositorySource.getSavedBook()
        mView.bindResponse(bookDetailsData!!)
    }

    lateinit var mRepositorySource: RepositorySource
    lateinit var bookDetails: Book
}