package com.audiospotapplication.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.view.ContextThemeWrapper
import com.audiospotapplication.R
import java.util.*

class ApplicationLanguageHelper(base: Context) : ContextThemeWrapper(base, R.style.AppTheme) {

    companion object {
        fun wrap(context: Context, language: String): ContextThemeWrapper {
            var context = context
            val config = context.resources.configuration
            if (language != "") {
                val locale = Locale(language)
                Locale.setDefault(locale)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setSystemLocale(config, locale)
                } else {
                    setSystemLocaleLegacy(config, locale)
                }
                config.setLayoutDirection(locale)
                context.resources.updateConfiguration(config, context.resources.displayMetrics)
            }
            return ApplicationLanguageHelper(context)
        }

        @SuppressWarnings("deprecation")
        fun setSystemLocaleLegacy(config: Configuration, locale: Locale) {
            config.locale = locale
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun setSystemLocale(config: Configuration, locale: Locale) {
            config.setLocale(locale)
        }
    }
}