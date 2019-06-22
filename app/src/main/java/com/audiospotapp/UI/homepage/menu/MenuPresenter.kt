package com.audiospotapp.UI.homepage.menu

import com.audiospot.DataLayer.Model.AuthResponse
import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Model.LogoutAuthResponse
import com.audiospotapp.DataLayer.Retrofit.GlobalKeys
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapp.DataLayer.Retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class MenuPresenter(val mView: MenuContract.View) : MenuContract.Presenter {

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
    }

    lateinit var mRepositorySource: RepositorySource

    override fun handleProfilePressed() {
        if (mRepositorySource.getAuthResponse() == null) {
            mView.showShouldBeLoggedInMessage()
        } else {
            mView.showProfilePageScreen()
        }
    }

    override fun handleMyFavouritePressed() {
        if (mRepositorySource.getAuthResponse() == null) {
            mView.showShouldBeLoggedInMessage()
        } else {
            mView.showMyFavouriteScreen()
        }
    }

    override fun handleSettingsPressed() {
        if (mRepositorySource.getAuthResponse() == null) {
            mView.showShouldBeLoggedInMessage()
        } else {
            mView.showSettingsScreen()
        }
    }

    override fun handleReceviveGiftPressed() {

    }

    override fun handleTermsAndConditionsPressed() {

    }

    override fun handleContactUsPressed() {
        if (mRepositorySource.getAuthResponse() == null) {
            mView.showShouldBeLoggedInMessage()
        } else {
            mView.showContactUsScreen()
        }
    }

    override fun handleAboutUsPressed() {

    }

    override fun handleMyBookmarksClicked() {

    }

    override fun handleSignoutPressed() {

        if (mRepositorySource.getAuthResponse() == null) {
            mView.showShouldBeLoggedInMessage()
        } else {
            mView.showLoading()
            mRepositorySource.signOut(object : RetrofitCallbacks.LogoutAuthResponseCallback {
                override fun onSuccess(result: LogoutAuthResponse?) {
                    mView.dismissLoading()
                    val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                    if (status == RetrofitResponseHandler.Status.VALID) {
                        mView.showLoginScreen()
                    } else {
                        mView!!.showErrorMessage()
                    }
                }

                override fun onFailure(call: Call<LogoutAuthResponse>?, t: Throwable?) {
                    mView!!.showErrorMessage()
                }
            })
        }
    }

    override fun handleNotificationsPressed() {
        if (mRepositorySource.getAuthResponse() == null) {
            mView.showShouldBeLoggedInMessage()
        } else {

        }
    }
}