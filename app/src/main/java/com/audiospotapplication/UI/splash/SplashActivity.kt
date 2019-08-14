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
import android.R.attr.versionName
import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class SplashActivity : AppCompatActivity(), SplashContract.View {

    override fun getActivity(): AppCompatActivity {
        return this@SplashActivity
    }

    private val REQUEST_CODE_UPDATE: Int = 1000
    private val PERMISSION_CODE: Int = 10

    private val SPLASH_DISPLAY_LENGTH = 2000

    lateinit var mPresenter: SplashContract.Presenter

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CODE &&
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            val storage = Storage(applicationContext)
            val path = storage.internalCacheDirectory

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
                // For a flexible update, use AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
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
}