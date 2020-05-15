package com.audiospotapplication.UI.settings

import android.content.Context
import com.audiospotapplication.UI.BaseView

interface SettingsContract {

    interface Presenter {
        fun start()
        fun handleDurationPressed()
        fun handleAppLanguagePressed()
        fun handleLanguageSelection(strName: String?)
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun showLanguageListDialog()
        fun restartAppWithDifferentLanguage(lang: String)
    }
}