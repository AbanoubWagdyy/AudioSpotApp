package com.audiospotapp.UI.forgetPassword

import com.audiospot.DataLayer.Model.AuthResponse
import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapp.DataLayer.Retrofit.RetrofitResponseHandler
import com.audiospotapp.utils.EmailUtils
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class ForgetPasswordPresenter(val mView: ForgetPasswordContract.View) : ForgetPasswordContract.Presenter {

    lateinit var mRepositorySource: RepositorySource

    override fun onCancelPressed() {
        mView.finishScreen()
    }

    override fun onResetPasswordClicked(email: String) {
        val isEmailValid = EmailUtils.isValidEmail(email)
        if (isEmailValid) {
            mRepositorySource = DataRepository.getInstance(mView.getActivity().applicationContext)
            mRepositorySource.resetPassword(email,object : RetrofitCallbacks.AuthResponseCallback{
                override fun onSuccess(result: AuthResponse?) {
                    val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                    if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                        mView!!.viewLoginScreen()
                    } else {
                        mView!!.showErrorMessage(result!!.message)
                    }
                }

                override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                    mView!!.showErrorMessage("Please try again Later")
                }

            })
        }else{
            mView.showInvalidEmailAddressMessage("Invalid Email Address ,please try valid one !.")
        }
    }
}