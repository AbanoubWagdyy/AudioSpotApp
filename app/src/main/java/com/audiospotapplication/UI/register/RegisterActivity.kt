package com.audiospotapplication.UI.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.audiospotapplication.R
import com.audiospotapplication.UI.homepage.HomepageActivity
import com.audiospotapplication.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.etPassword

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