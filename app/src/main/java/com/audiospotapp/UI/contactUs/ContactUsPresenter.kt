package com.audiospotapp.UI.contactUs

import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Model.ContactUsResponse
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapp.DataLayer.Retrofit.RetrofitResponseHandler
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
                }
            }

            override fun onFailure(call: Call<ContactUsResponse>?, t: Throwable?) {
                mView.dismissLoading()
                mView.showMessage("Please check your internet connection")
            }
        })
    }

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
    }

    lateinit var mRepositorySource: RepositorySource
}