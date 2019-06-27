package com.audiospotapplication.UI.login

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.audiospotapplication.R
import com.audiospotapplication.UI.forgetPassword.ForgetPasswordActivity
import com.audiospotapplication.UI.homepage.HomepageActivity
import com.audiospotapplication.UI.register.RegisterActivity
import com.audiospotapplication.utils.DialogUtils
import com.audiospotapplication.utils.FacebookHelper
import com.audiospotapplication.utils.GoogleSignInHelper
import com.facebook.GraphResponse
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity(), LoginContract.View, FacebookHelper.OnFbSignInListener,
    GoogleSignInHelper.OnGoogleSignInListener {

    override fun showProgressDialog() {
        DialogUtils.showProgressDialog(this, "Loading ....")
    }

    override fun hideProgressDialog() {
        DialogUtils.dismissProgressDialog()
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun viewIncorrectDataMessage() {
        Snackbar.make(findViewById(android.R.id.content), "Please correct the above data", Snackbar.LENGTH_SHORT).show()
    }

    override fun getActivity(): AppCompatActivity {
        return this
    }

    lateinit var mPresenter: LoginContract.Presenter
    private var fbConnectHelper: FacebookHelper? = null
    private var googleSignInHelper: GoogleSignInHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val info = packageManager.getPackageInfo(
                "com.audiospotapplication",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }

        setContentView(R.layout.activity_login)


        fbConnectHelper = FacebookHelper(this, this)

        googleSignInHelper = GoogleSignInHelper(this, this)
        googleSignInHelper!!.connect()

        setListeners()
        mPresenter = LoginPresenter(this)
        mPresenter.start()
    }

    override fun viewForgetPasswordScreen() {
        val intent = Intent(applicationContext, ForgetPasswordActivity::class.java)
        startActivity(intent)
    }

    override fun viewHomepageScreen() {
//        googleSignInHelper!!.signOut()
//        fbConnectHelper!!.logout()
        val intent = Intent(applicationContext, HomepageActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun viewRegisterScreen() {
        val intent = Intent(applicationContext, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun setListeners() {

        btnLogin.setOnClickListener {
            mPresenter.login(etUsername.text.toString(), etPassword.text.toString())
        }

        btnLoginWithFacebook.setOnClickListener {
            fbConnectHelper!!.connect()
        }

        btnLoginWithGoogle.setOnClickListener {
            googleSignInHelper!!.signIn()
        }

        tvForgetPassword.setOnClickListener {
            mPresenter.validateForgetPasswordClicked()
        }

        tvSignUp.setOnClickListener {
            mPresenter.validateSignUpClicked()
        }
        tvSkip.setOnClickListener {
            mPresenter.validateSkipClicked()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googleSignInHelper!!.onActivityResult(requestCode, resultCode, data)
        fbConnectHelper!!.onActivityResult(requestCode, resultCode, data)
    }

    override fun OnFbSignInComplete(graphResponse: GraphResponse?, error: String?) {
        if (error == null) {
            if (graphResponse != null) {
                try {
                    fbConnectHelper!!.logout()
//                    val profileImg = "http://graph.facebook.com/$id/picture?type=large"
                    val jsonObject = graphResponse.jsonObject
                    mPresenter.handleSocialLoginPressed(
                        jsonObject.getString("first_name"),
                        jsonObject.getString("last_name"),
                        jsonObject.getString("email"),
                        "facebook",
                        jsonObject.getString("id")
                    )
                } catch (e: JSONException) {
                    Log.i("", e.message)
                }
            }
        }
    }

    override fun OnGSignInSuccess(googleSignInAccount: GoogleSignInAccount?) {
        if (googleSignInAccount != null) {
            var first_name = ""
            if (googleSignInAccount.givenName != null)
                first_name = googleSignInAccount.givenName!!

            var last_name = ""
            if (googleSignInAccount.familyName != null)
                last_name = googleSignInAccount.familyName!!

            mPresenter.handleSocialLoginPressed(
                first_name,
                last_name,
                googleSignInAccount.email!!,
                "google",
                googleSignInAccount.id!!
            )
        }
    }

    override fun OnGSignInError(error: String?) {
        Snackbar.make(findViewById(android.R.id.content), error!!, Snackbar.LENGTH_SHORT).show()
    }
}