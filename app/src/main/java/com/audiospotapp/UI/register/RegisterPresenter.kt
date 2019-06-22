package com.audiospotapp.UI.register

import com.audiospot.DataLayer.Model.AuthResponse
import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapp.DataLayer.Retrofit.RetrofitResponseHandler
import com.audiospotapp.utils.DialogUtils
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class RegisterPresenter(val mView: RegisterContract.View) : RegisterContract.Presenter {

    lateinit var mRepositorySource: RepositorySource

    override fun register(
        first_name: String,
        last_name: String,
        email: String,
        mobile_phone: String,
        password: String,
        confirm_password: String
    ) {
//        val isValid = validateForm(first_name, last_name, email, mobile_phone, password, confirm_password)
//        if (isValid) {
        mView.showProgressDialog()
        mRepositorySource = DataRepository.getInstance(mView.getActivity().applicationContext)
        mRepositorySource.register(
            first_name,
            last_name,
            email,
            mobile_phone,
            password, object : RetrofitCallbacks.AuthResponseCallback {
                override fun onSuccess(result: AuthResponse?) {
                    mView.hideProgressDialog()
                    val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                    if (status == RetrofitResponseHandler.Status.VALID) {
                        mView!!.viewHomepageScreen()
                    } else {
                        mView!!.showErrorMessage(result!!.message)
                    }
                }

                override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                    mView.hideProgressDialog()
                    mView!!.showErrorMessage("Please try again later")
                }
            }
        )
    }

    private fun validateForm(
        first_name: String,
        last_name: String,
        email: String,
        mobile_phone: String,
        password: String,
        confirm_password: String
    ): Boolean {

        val isValidFirstName = first_name.isNotBlank() && first_name.isNotEmpty()
        val isValidLastName = last_name.isNotBlank() && last_name.isNotEmpty()
        val isValidEmail = email.isNotBlank() && email.isNotEmpty() && isValidEmail(email)
        val isValidPhone = mobile_phone.isNotBlank() && mobile_phone.isNotEmpty()
        val isValidPassword = password.isNotBlank() && first_name.isNotEmpty()
        val isValidConfirmPassword = first_name.isNotBlank() && first_name.isNotEmpty()
        val isPasswordMatched = password.equals(confirm_password)

        return isValidFirstName && isValidLastName && isValidEmail && isValidPassword && isValidConfirmPassword && isPasswordMatched && isValidPhone
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}