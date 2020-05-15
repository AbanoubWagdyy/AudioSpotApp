package com.audiospotapplication.ui.settings

import android.content.Context
import com.audiospotapplication.ui.BaseView

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