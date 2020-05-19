package com.audiospotapplication.ui.settings

import android.content.Context
import android.content.res.Configuration
import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.RepositorySource
import com.audiospotapplication.data.retrofit.GlobalKeys
import java.util.*

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

        mView.getAppContext()?.let { updateResources(it, lang) }
        mRepositorySource.setCurrentLanguage(lang)
        mView.restartAppWithDifferentLanguage(lang)
    }

    private fun updateResources(context: Context,language : String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        // Create a new configuration object
        // Create a new configuration object
        val config = Configuration()
        // Set the locale of the new configuration
        // Set the locale of the new configuration
        config.locale = locale
        // Update the configuration of the Accplication context
        // Update the configuration of the Accplication context
        context.getResources().updateConfiguration(
            config,
            context.getResources().getDisplayMetrics()
        )
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