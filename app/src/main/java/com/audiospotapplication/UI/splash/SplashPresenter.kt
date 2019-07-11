package com.audiospotapplication.UI.splash

import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Retrofit.GlobalKeys
import com.visionvalley.letuno.DataLayer.RepositorySource

class SplashPresenter(val mView: SplashContract.View) : SplashContract.Presenter {

    lateinit var mRepository: RepositorySource

    override fun start() {
        mRepository = DataRepository.getInstance(mView.getActivity().applicationContext)
//        if (mRepository.getStringFromCache(GlobalKeys.StoreData.TOKEN, null) == null) {
//            FirebaseInstanceId.getInstance().instanceId
//                .addOnCompleteListener(OnCompleteListener { task ->
//                    if (!task.isSuccessful) {
//                        return@OnCompleteListener
//                    }
//                    val token = task.result?.token
//                    mRepository.setStringIntoCache(GlobalKeys.StoreData.TOKEN, token)
//                    validateLogging()
//                })
//        } else {
//            validateLogging()
//        }
        validateLogging()
    }

    private fun validateLogging() {
        if (mRepository.getBooleanFromCache(GlobalKeys.StoreData.IS_LOGGED, false)) {
            mView.startHomepageScreen()
        } else {
            mView.startLoginScreen()
        }
    }
}