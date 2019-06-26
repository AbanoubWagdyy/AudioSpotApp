package com.audiospotapplication

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
//import com.github.windsekirun.rxsociallogin.facebook.FacebookConfig
//import com.github.windsekirun.rxsociallogin.initSocialLogin
import okhttp3.Cache

class AudioSpotApp : Application() {

    override fun onCreate() {
        super.onCreate()
//        initSocialLogin {
//            facebook(getString(R.string.facebook_app_id)) {
//                behaviorOnCancel = true
//                requireWritePermissions = false
//                imageEnum = FacebookConfig.FacebookImageEnum.Large
//                requireEmail = true
//            }
//            google(getString(R.string.google_api_key)) {
//                requireEmail = true
//            }
//        }
    }

    companion object {
        private val CACHE_SIZE = 20 * 1024 * 1024
        private val context: Context? = null
        private var instance: AudioSpotApp? = null

        val isNetworkAvailable: Boolean
            get() {
                val cm = instance!!.applicationContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork = cm.activeNetworkInfo
                return activeNetwork != null && activeNetwork.isConnectedOrConnecting
            }

        val networkCache: Cache
            get() = Cache(instance!!.cacheDir, CACHE_SIZE.toLong())
    }
}