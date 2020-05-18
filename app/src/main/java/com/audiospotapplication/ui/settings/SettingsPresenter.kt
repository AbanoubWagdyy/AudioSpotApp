package com.audiospotapplication.ui.settings

import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.retrofit.GlobalKeys
import com.audiospotapplication.data.RepositorySource

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