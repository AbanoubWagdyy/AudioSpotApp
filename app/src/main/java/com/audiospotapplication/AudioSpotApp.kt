package com.audiospotapplication

import android.app.Application
import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.model.BaseViewModel
import com.audiospotapplication.ui.homepage.HomepageViewModel
import com.audiospotapplication.ui.homepage.home.HomeViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.visionvalley.letuno.DataLayer.RepositorySource
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class AudioSpotApp : Application() {

    val appModule = module {
        single<RepositorySource> { DataRepository.getInstance((androidContext())) }
    }

    val viewModelModule = module {
        viewModel { BaseViewModel() }
        viewModel { HomepageViewModel() }
        viewModel { HomeViewModel() }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@AudioSpotApp.applicationContext)
            modules(listOf(appModule, viewModelModule))
        }

        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true)
    }
}