package com.audiospotapplication.ui.updateProfile

import com.audiospot.DataLayer.Model.AuthResponse
import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.retrofit.RetrofitCallbacks
import com.audiospotapplication.data.retrofit.RetrofitResponseHandler
import com.audiospotapplication.utils.EmailUtils
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class UpdateProfilePresenter(val mView: UpdateProfileContract.View) : UpdateProfileContract.Presenter {

    override fun handleChangePasswordClicked() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
        var authResponse = mRepositorySource.getAuthResponse()
        if (authResponse!!.data.Password.equals("")) {
            mView.disableChangePasswordClick()
        } else {
            mView.setChangePasswordClick()
        }
    }

    override fun updateProfile(first_name: String, last_name: String, email: String, mobile_phone: String) {
        if (isFieldsValid(first_name, last_name, email, mobile_phone)) {
            if (EmailUtils.isValidEmail(email)) {
                mView.showLoading()
                mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
                mRepositorySource.updateProfile(
                    first_name,
                    last_name,
                    email,
                    mobile_phone,
                    object : RetrofitCallbacks.AuthResponseCallback {
                        override fun onSuccess(result: AuthResponse?) {
                            mView.dismissLoading()
                            val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                            if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                                mView!!.finalizeView()
                            } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                                mView!!.showLoginPage()
                            } else {
                                mView!!.showErrorMessage(result!!.message)
                            }
                        }

                        override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                            mView.dismissLoading()
                            mView!!.showErrorMessage("Please Check your internet connection")
                        }
                    })
            } else {
                mView.showInvalidEmailMessage("Please Enter Valid Email Address")
            }
        }
    }

    private fun isFieldsValid(
        first_name: String,
        last_name: String,
        email: String,
        mobile_phone: String
    ): Boolean {
        return !first_name.isNullOrEmpty() &&
                !last_name.isNullOrEmpty() &&
                !mobile_phone.isNullOrEmpty() &&
                !email.isNullOrEmpty()
    }

    lateinit var mRepositorySource: RepositorySource
}