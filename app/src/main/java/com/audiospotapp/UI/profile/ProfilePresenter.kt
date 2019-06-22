package com.audiospotapp.UI.profile

import com.audiospot.DataLayer.Model.AuthResponse
import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Model.ProfileResponse
import com.audiospotapp.DataLayer.Retrofit.GlobalKeys
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class ProfilePresenter(val mView: ProfileContract.View) : ProfileContract.Presenter {

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
        mRepositorySource.getProfile(object : RetrofitCallbacks.ProfileResponseCallback {
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

    lateinit var mRepositorySource: RepositorySource
}