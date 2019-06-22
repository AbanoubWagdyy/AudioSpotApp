package com.audiospotapp.UI.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import com.audiospotapp.UI.login.LoginActivity
import com.audiospotapp.R
import com.audiospotapp.UI.homepage.HomepageActivity


class SplashActivity : AppCompatActivity(), SplashContract.View {

    override fun getActivity(): AppCompatActivity {
        return this@SplashActivity
    }

    private val SPLASH_DISPLAY_LENGTH = 3000

    lateinit var mPresenter: SplashContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mPresenter = SplashPresenter(this)
        mPresenter.start()
    }

    override fun startHomepageScreen() {
        val intent = Intent(this@SplashActivity, HomepageActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun startLoginScreen() {
        Handler().postDelayed({
            val mainIntent = Intent(this@SplashActivity, LoginActivity::class.java)
            this@SplashActivity.startActivity(mainIntent)
            this@SplashActivity.finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }
}