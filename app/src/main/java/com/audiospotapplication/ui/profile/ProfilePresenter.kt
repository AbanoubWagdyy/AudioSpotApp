package com.audiospotapplication.ui.profile

import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.model.ProfileResponse
import com.audiospotapplication.data.retrofit.RetrofitCallbacks
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class ProfilePresenter(val mView: ProfileContract.View) : ProfileContract.Presenter {

    override fun start() {
        mRepositorySource = mView.getAppContext()?.let { DataRepository.getInstance(it) }
        mRepositorySource?.getProfile(object : RetrofitCallbacks.ProfileResponseCallback {
            override fun onSuccess(result: ProfileResponse?) {
                mView.setFullName(result!!.data.first_name + " " + result!!.data.last_name)
                mView.setEmail(result!!.data.email)
                mView.setMobilePhone(result!!.data.phone)
                mView.setUserImage(result!!.data.profile_photo)
            }

            override fun onFailure(call: Call<ProfileResponse>?, t: Throwable?) {
                mView.showErrorMessage("Internet Connection ,Please try again !.")
            }
        })
    }

    var mRepositorySource: RepositorySource? = null
}