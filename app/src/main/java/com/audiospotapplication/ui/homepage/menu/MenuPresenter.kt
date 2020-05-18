package com.audiospotapplication.ui.homepage.menu

import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.model.LogoutAuthResponse
import com.audiospotapplication.data.retrofit.RetrofitCallbacks
import com.audiospotapplication.data.retrofit.RetrofitResponseHandler
import com.audiospotapplication.data.RepositorySource
import retrofit2.Call

class MenuPresenter(val mView: MenuContract.View) : MenuContract.Presenter {

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
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
        mView.showVoucherScreen()
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
        if (mRepositorySource.getAuthResponse() == null) {
            mView.showShouldBeLoggedInMessage()
        } else {
            mView.showMyBookmarksScreen()
        }
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
                    if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                        mView.showLoginScreen()
                    } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                        mView!!.showLoginPage()
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