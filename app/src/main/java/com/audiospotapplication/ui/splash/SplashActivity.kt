package com.audiospotapplication.ui.splash

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Handler
import com.audiospotapplication.ui.login.LoginActivity
import com.audiospotapplication.R
import com.audiospotapplication.ui.homepage.HomepageActivity
import java.io.File
import com.snatik.storage.Storage
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import java.util.*


class SplashActivity : AppCompatActivity(), SplashContract.View {

    override fun getActivity(): AppCompatActivity {
        return this@SplashActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        forceUpdate()
    }

    override fun startHomepageScreen() {
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, HomepageActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }

    override fun startLoginScreen() {

        var permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE
        )

        requestPermissions(permissions, PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CODE &&
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            val storage = Storage(applicationContext)
            val path = storage.internalFilesDirectory

            val newDir = path + File.separator + "AudioSpotDownloadsCache"
            storage.createDirectory(newDir)
            Handler().postDelayed({
                val mainIntent = Intent(this@SplashActivity, LoginActivity::class.java)
                this@SplashActivity.startActivity(mainIntent)
                this@SplashActivity.finish()
            }, SPLASH_DISPLAY_LENGTH.toLong())
        } else {
            finish()
        }
    }

    fun forceUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    REQUEST_CODE_UPDATE
                )
            } else {
                mPresenter = SplashPresenter(this@SplashActivity)
                mPresenter.start()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UPDATE) {
            if (requestCode == RESULT_OK) {
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

    private val REQUEST_CODE_UPDATE: Int = 1000
    private val PERMISSION_CODE: Int = 10
    private val SPLASH_DISPLAY_LENGTH = 2000
    lateinit var mPresenter: SplashContract.Presenter
}