package com.audiospotapplication.UI.contactUs

import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Model.ContactUsResponse
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.DataLayer.Retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class ContactUsPresenter(val mView: ContactUsContract.View) : ContactUsContract.Presenter {

    override fun contactUs(message: String) {
        mView.showLoadingDialog()
        mRepositorySource.contactUs(message, object : RetrofitCallbacks.ContactUsResponseCallback {

            override fun onSuccess(result: ContactUsResponse?) {
                mView.dismissLoading()
                if (result != null) {
                    mView.showMessage(result.status.message)
                    val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                    if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                        mView.showHompageScreen()
                    } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                        mView!!.showLoginPage()
                    }
                }
            }

            override fun onFailure(call: Call<ContactUsResponse>?, t: Throwable?) {
                mView.dismissLoading()
                mView.showMessage("Please check your internet connection")
            }
        })
    }

    override fun start() {
        mRepositorySource = mView.getAppContext()?.let { DataRepository.getInstance(it) }!!
    }

    lateinit var mRepositorySource: RepositorySource
}