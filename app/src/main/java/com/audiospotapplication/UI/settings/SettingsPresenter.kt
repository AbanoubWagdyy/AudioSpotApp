package com.audiospotapplication.UI.settings

import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Retrofit.GlobalKeys
import com.visionvalley.letuno.DataLayer.RepositorySource

class SettingsPresenter(val mView: SettingsContract.View) : SettingsContract.Presenter {

    override fun handleLanguageSelection(strName: String?) {
        var lang = ""
        lang = if (strName!!.equals(GlobalKeys.Language.ENGLISH))
            "en"
        else
            "ar"

        var currentLang = mRepositorySource.getCurrentLanguage()
        if (currentLang.equals(lang))
            return

        mRepositorySource.setCurrentLanguage(lang)
        mView.restartAppWithDifferentLanguage(lang)
    }

    override fun handleDurationPressed() {

    }

    override fun handleAppLanguagePressed() {
        mView.showLanguageListDialog()
    }

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
    }

    lateinit var mRepositorySource: RepositorySource
}