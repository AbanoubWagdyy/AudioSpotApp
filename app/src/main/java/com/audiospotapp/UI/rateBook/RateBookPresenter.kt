package com.audiospotapp.UI.rateBook

import com.audiospot.DataLayer.Model.Book
import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Model.Response
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapp.DataLayer.Retrofit.RetrofitResponseHandler
import com.audiospotapp.utils.EmailUtils
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