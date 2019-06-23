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

    override fun giveGift(email: String) {
        val isValidEmail = EmailUtils.isValidEmail(email)
        if (isValidEmail) {
            mView.showLoadingDialog()
            mRepositorySource.sendGift(email, object : RetrofitCallbacks.ResponseCallback {
                override fun onSuccess(result: Response?) {
                    mView.dismissLoading()
                    mView.showMessage(result!!.message)
                    val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                    if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                       mView.finalizeView()
                    }
                }

                override fun onFailure(call: Call<Response>?, t: Throwable?) {
                    mView.dismissLoading()
                    mView.showMessage("Please try again later")
                }

                override fun onAuthFailure() {

                }

            })
        } else {
            mView.showInvalidEmailMessage("Invalid Email Address")
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