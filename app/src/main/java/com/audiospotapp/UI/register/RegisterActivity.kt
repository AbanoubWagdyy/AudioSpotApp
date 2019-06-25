package com.audiospotapp.UI.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.audiospotapp.R
import com.audiospotapp.UI.homepage.HomepageActivity
import com.audiospotapp.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.etPassword
import com.audiospotapp.UI.ActiveTab

class RegisterActivity : AppCompatActivity(), RegisterContract.View {

    override fun showProgressDialog() {
        DialogUtils.showProgressDialog(this,"Loading ....")
    }

    override fun hideProgressDialog() {
        DialogUtils.dismissProgressDialog()
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun viewIncorrectDataMessage() {
        Snackbar.make(findViewById(android.R.id.content), "Please fill the above data", Snackbar.LENGTH_SHORT).show()
    }

    override fun viewHomepageScreen() {
        val intent = Intent(applicationContext, HomepageActivity::class.java)
        startActivity(intent)
    }


    override fun getActivity(): AppCompatActivity {
        return this
    }

    lateinit var mPresenter: RegisterContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mPresenter = RegisterPresenter(this)

        btnRegister.setOnClickListener {
            mPresenter.register(
                etFirstName.text.toString(),
                etLastName.text.toString(),
                etEmail.text.toString(),
                etMobilePhone.text.toString(),
                etPassword.text.toString(),
                etConfirmPassword.text.toString()
            )
        }
    }
}