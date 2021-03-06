package com.audiospotapplication.ui.homepage.home

import androidx.lifecycle.MutableLiveData
import com.audiospot.DataLayer.Model.Book
import com.audiospot.DataLayer.Model.HomepageRepsonse
import com.audiospotapplication.data.model.BaseViewModel
import com.audiospotapplication.data.retrofit.RetrofitCallbacks
import com.audiospotapplication.data.retrofit.RetrofitResponseHandler
import retrofit2.Call

class HomeViewModel : BaseViewModel() {

    init {
        getHomepageBooks()
    }

    fun getHomepageBooks() {
        loadingObserver.value = true
        mRepositorySource.getHomepage(object : RetrofitCallbacks.HomepageResponseCallback {
            override fun onSuccess(result: HomepageRepsonse?) {
                loadingObserver.value = false
                val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                    homepageResponseObserver.value = result
                } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                    authenticationObserver.value =
                        RetrofitResponseHandler.Companion.Status.UNAUTHORIZED
                } else {
                    errorObserver.value = result!!.message
                }
            }

            override fun onFailure(call: Call<HomepageRepsonse>?, t: Throwable?) {
                loadingObserver.value = false
                errorObserver.value = "Please check the internet connection !."
            }
        })
    }

    fun getHomepageBooksLiveData(): MutableLiveData<HomepageRepsonse> {
        return homepageResponseObserver
    }


    fun getErrorObserverLiveData(): MutableLiveData<String> {
        return errorObserver
    }

    fun saveBook(book: Book) {
        mRepositorySource.saveBook(book)
    }

    fun getHomepageResponse(): HomepageRepsonse {
        return homepageResponseObserver.value!!
    }

    var homepageResponseObserver = MutableLiveData<HomepageRepsonse>()
}