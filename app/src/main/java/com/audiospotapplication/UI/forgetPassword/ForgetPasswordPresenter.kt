package com.audiospotapplication.UI.forgetPassword

import com.audiospot.DataLayer.Model.AuthResponse
import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Model.Response
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.DataLayer.Retrofit.RetrofitResponseHandler
import com.audiospotapplication.utils.EmailUtils
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class ForgetPasswordPresenter(val mView: ForgetPasswordContract.View) : ForgetPasswordContract.Presenter {

    override fun resetRepo() {
        mRepositorySource.clear()
    }

    lateinit var mRepositorySource: RepositorySource

    override fun onCancelPressed() {
        mView.finishScreen()
    }

    override fun onResetPasswordClicked(email: String) {
        val isEmailValid = EmailUtils.isValidEmail(email)
        if (isEmailValid) {
            mView.showProgressDialog()
            mRepositorySource = DataRepository.getInstance(mView.getActivity().applicationContext)
            mRepositorySource.resetPassword(email, object : RetrofitCallbacks.ResponseCallback {
                override fun onSuccess(result: Response?) {
                    mView.hideProgressDialog()
                    val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                    mView!!.showErrorMessage(result!!.message)
                    if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                        mView!!.viewLoginScreen()
                    } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                        mView!!.showLoginPage()
                    }
                }

                override fun onFailure(call: Call<Response>?, t: Throwable?) {
                    mView.hideProgressDialog()
                }

                override fun onAuthFailure() {
                }
            })
        } else {
            mView.showInvalidEmailAddressMessage("Invalid Email Address ,please try valid one !.")
        }
    }
}