package com.audiospotapplication.data.model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audiospotapplication.data.retrofit.RetrofitResponseHandler
import com.audiospotapplication.data.RepositorySource
import io.reactivex.disposables.Disposable
import org.koin.core.KoinComponent
import org.koin.core.inject

open class BaseViewModel : ViewModel(), KoinComponent {

    var loadingObserver = MutableLiveData<Boolean>()

    var disposable: Disposable? = null

    var errorObserver = MutableLiveData<String>()

    val context: Context by inject()

    val mRepositorySource: RepositorySource by inject()

    val authenticationObserver = MutableLiveData<RetrofitResponseHandler.Companion.Status>()

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun getObserverForError(): MutableLiveData<String> {
        return errorObserver
    }

    fun getProgressLoadingObserver(): MutableLiveData<Boolean> {
        return loadingObserver
    }

    fun getErrorAuthenticationObserver(): MutableLiveData<RetrofitResponseHandler.Companion.Status> {
        return authenticationObserver
    }

    fun clearData() {
        mRepositorySource.clear()
    }
}