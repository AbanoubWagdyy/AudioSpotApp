package com.audiospotapplication.UI.splash

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.widget.Toast
import com.audiospotapplication.UI.login.LoginActivity
import com.audiospotapplication.R
import com.audiospotapplication.UI.homepage.HomepageActivity
import java.io.File
import com.snatik.storage.Storage


class SplashActivity : AppCompatActivity(), SplashContract.View {

    override fun getActivity(): AppCompatActivity {
        return this@SplashActivity
    }

    private val PERMISSION_CODE: Int = 10

    private val SPLASH_DISPLAY_LENGTH = 2000

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

        var permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE
        )

        requestPermissions(permissions, PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CODE &&
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            val storage = Storage(applicationContext)
            val path = storage.internalCacheDirectory

            val newDir = path + File.separator + "AudioSpotDownloadsCache"
//            val isCreated = storage.createDirectory(newDir)
            Handler().postDelayed({
                val mainIntent = Intent(this@SplashActivity, LoginActivity::class.java)
                this@SplashActivity.startActivity(mainIntent)
                this@SplashActivity.finish()
            }, SPLASH_DISPLAY_LENGTH.toLong())
        } else {
            finish()
        }
    }
}