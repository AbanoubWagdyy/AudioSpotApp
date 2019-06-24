package com.audiospotapp.UI.giveAgift

import com.audiospot.DataLayer.Model.Book
import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Model.Response
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapp.DataLayer.Retrofit.RetrofitResponseHandler
import com.audiospotapp.utils.EmailUtils
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class GiveGiftPresenter(val mView: GiveGiftContract.View) : GiveGiftContract.Presenter {

    override fun giveGift(email1: String, email2: String, email3: String, email4: String, email5: String) {
        mView.showLoadingDialog()
        mRepositorySource.sendAsGift(
            email1,
            email2,
            email3,
            email4,
            email5,
            object : RetrofitCallbacks.ResponseCallback {
                override fun onSuccess(result: Response?) {
                    mView.showMessage(result!!.message)
                    var status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                    if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                        mRepositorySource.addBookToCart(object : RetrofitCallbacks.ResponseCallback {
                            override fun onSuccess(result1: Response?) {
                                mView.dismissLoading()
                                status = RetrofitResponseHandler.validateAuthResponseStatus(result1)
                                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                                    mView.showCartScreen()
                                } else {
                                    mView.showMessage(result1!!.message)
                                }
                            }

                            override fun onFailure(call: Call<Response>?, t: Throwable?) {

                            }

                            override fun onAuthFailure() {

                            }

                        })
                    }else{
                        mView.dismissLoading()
                    }
                }

                override fun onFailure(call: Call<Response>?, t: Throwable?) {
                    mView.dismissLoading()
                    mView.showMessage("Please try again later")
                }

                override fun onAuthFailure() {

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