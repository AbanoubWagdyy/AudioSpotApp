package com.audiospotapplication.UI.changePassword

import com.audiospot.DataLayer.Model.AuthResponse
import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.DataLayer.Retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class ChangePasswordPresenter(val mView: ChangePasswordContract.View) : ChangePasswordContract.Presenter {

    override fun updatePassword(old_password: String, new_password: String, confirm_password: String) {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
        if (isFieldsValid(old_password, new_password, confirm_password)) {
            if (isValidOldPassword(old_password)) {
                mView.showLoading()
                mRepositorySource.updatePassword(
                    old_password,
                    new_password,
                    confirm_password,
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
                mView.showIncorrectOldPassword("Please Validate Your Old Password")
            }
        } else {
            mView.showCompleteAllFieldsMessage("Please Complete All Fields")
        }
    }

    private fun isValidOldPassword(old_password: String): Boolean {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
        var authResponse = mRepositorySource.getAuthResponse()
        return old_password.equals(authResponse!!.data.Password)
    }

    private fun isFieldsValid(
        old_password: String,
        new_password: String,
        confirm_password: String
    ): Boolean {
        return !old_password.isNullOrEmpty() &&
                !new_password.isNullOrEmpty() &&
                !confirm_password.isNullOrEmpty()
    }

    lateinit var mRepositorySource: RepositorySource
}