package com.audiospotapplication

import android.app.Application
import android.content.Context

class AudioSpotApp : Application() {

    private val context: Context? = null

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
}