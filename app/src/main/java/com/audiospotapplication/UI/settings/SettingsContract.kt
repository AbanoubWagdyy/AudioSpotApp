package com.audiospotapplication.UI.settings

import android.content.Context

interface SettingsContract {

    interface Presenter {
        fun start()
        fun handleDurationPressed()
        fun handleAppLanguagePressed()
        fun handleLanguageSelection(strName: String?)
    }

    interface View {
        fun getAppContext(): Context?
        fun showLanguageListDialog()
        fun restartAppWithDifferentLanguage(lang: String)
    }
}