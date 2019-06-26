package com.audiospotapplication.UI.giftSelection

import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Model.Response
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.DataLayer.Retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class GiftSelectionPresenter(val mView: GiftSelectionContract.View) : GiftSelectionContract.Presenter {

    override fun handleSubmitClicked(giftSelection: GiftSelection, quantity: Int) {
        mRepositorySource.submitGiftProperities(giftSelection, quantity)
        when (giftSelection) {
            GiftSelection.AUDIOSPOT_ACCOUNT -> {
                mView.showGiftSCreen()
            }
            GiftSelection.VOUCHER -> {
                mView.showLoadingDialog()
                mRepositorySource.addBookToCart(object : RetrofitCallbacks.ResponseCallback {
                    override fun onSuccess(result1: Response?) {
                        mView.dismissLoading()
                        var status = RetrofitResponseHandler.validateAuthResponseStatus(result1)
                        if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                            mView.showCartScreen()
                        } else {
                            mView.showMessage(result1!!.message)
                        }
                    }

                    override fun onFailure(call: Call<Response>?, t: Throwable?) {
                        mView.dismissLoading()
                    }

                    override fun onAuthFailure() {
                        mView.dismissLoading()
                    }
                })
            }
        }
    }

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
        var bookDetailsData = mRepositorySource.getSavedBook()
        mView.bindResponse(bookDetailsData!!)
    }

    lateinit var mRepositorySource: RepositorySource
    lateinit var bookDetails: Book
}