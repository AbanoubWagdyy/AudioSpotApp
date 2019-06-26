package com.audiospotapplication.UI.forgetPassword

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.audiospotapplication.R
import com.audiospotapplication.UI.login.LoginActivity
import com.audiospotapplication.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_forget_password.*

class ForgetPasswordActivity : AppCompatActivity(), ForgetPasswordContract.View {

    override fun showProgressDialog() {
        DialogUtils.showProgressDialog(this,"Loading ....")
    }

    override fun hideProgressDialog() {
        DialogUtils.dismissProgressDialog()
    }

    override fun finishScreen() {
        finish()
    }

    override fun viewLoginScreen() {
        val intent = Intent(this@ForgetPasswordActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    override fun showInvalidEmailAddressMessage(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    override fun getActivity(): Activity {
        return this
    }

    lateinit var mPresenter: ForgetPasswordContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        btnResetPassword.setTransformationMethod(null);
        btnCancel.setTransformationMethod(null);

        mPresenter = ForgetPasswordPresenter(this)

        btnCancel.setOnClickListener {
            mPresenter.onCancelPressed()
        }

        btnResetPassword.setOnClickListener {
            mPresenter.onResetPasswordClicked(email.text.toString())
        }

    }
}
