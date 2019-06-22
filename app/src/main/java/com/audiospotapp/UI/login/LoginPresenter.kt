package com.audiospotapp.UI.login

import com.audiospot.DataLayer.Model.AuthResponse
import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapp.DataLayer.Retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call


class LoginPresenter(var mView: LoginContract.View?) : LoginContract.Presenter {

    override fun handleSocialLoginPressed(
        firstName: String,
        lastName: String,
        email: String,
        platform: String,
        id: String
    ) {
        mView!!.showProgressDialog()
        mRepository.socialLogin(
            firstName,
            lastName,
            email,
            platform,
            id,
            object : RetrofitCallbacks.AuthResponseCallback {
                override fun onSuccess(result: AuthResponse?) {
                    mView!!.hideProgressDialog()
                    if (result != null) {
                        if (result.data != null) {
                            if (result!!.data.email.equals("")) {
                                result.data.email = email
                            }
                            val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                            if (status == RetrofitResponseHandler.Status.VALID) {
                                mView!!.viewHomepageScreen()
                            } else {
                                mView!!.showErrorMessage(result!!.message)
                            }
                        } else {
                            mView!!.showErrorMessage(result!!.message)
                        }
                    } else {
                        mView!!.showErrorMessage("Please try again later")
                    }
                }

                override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                    mView!!.hideProgressDialog()
                    mView!!.showErrorMessage("Please try again later")
                }
            })
    }

    override fun destroyView() {
        mView = null
    }

    lateinit var mRepository: RepositorySource

    override fun start() {
        mRepository = DataRepository.getInstance(mView!!.getActivity().applicationContext)
    }


    override fun login(username: String, password: String) {
        if (!username.isNullOrEmpty() || !password.isNullOrEmpty()) {
            mView!!.showProgressDialog()
            mRepository.login(username, password, object : RetrofitCallbacks.AuthResponseCallback {
                override fun onSuccess(result: AuthResponse?) {
                    mView!!.hideProgressDialog()
                    val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                    if (status == RetrofitResponseHandler.Status.VALID) {
                        mView!!.viewHomepageScreen()
                    } else {
                        mView!!.showErrorMessage(result!!.message)
                    }
                }

                override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                    mView!!.hideProgressDialog()
                    mView!!.showErrorMessage("Please try again later")
                }
            })
        } else {
            mView!!.viewIncorrectDataMessage()
        }
    }

    override fun validateForgetPasswordClicked() {
        if (mView != null)
            mView!!.viewForgetPasswordScreen()
    }

    override fun validateSignUpClicked() {
        if (mView != null)
            mView!!.viewRegisterScreen()
    }

    override fun validateSkipClicked() {
        if (mView != null)
            mView!!.viewHomepageScreen()
    }
}