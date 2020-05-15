package com.audiospotapplication.UI.homepage.home

import androidx.lifecycle.MutableLiveData
import com.audiospot.DataLayer.Model.Book
import com.audiospot.DataLayer.Model.HomepageRepsonse
import com.audiospotapplication.DataLayer.Model.BaseViewModel
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.DataLayer.Retrofit.RetrofitResponseHandler
import retrofit2.Call

class HomeViewModel : BaseViewModel() {

    init {
        getHomepageBooks()
    }

    private fun getHomepageBooks() {
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
                errorObserver.value = "Server Error,Please try again !."
            }
        })
    }

    fun getHomepageBooksLiveData(): MutableLiveData<HomepageRepsonse> {
        return homepageResponseObserver
    }

    fun saveBook(book: Book) {
        mRepositorySource.saveBook(book)
    }

    fun getHomepageResponse(): HomepageRepsonse {
        return homepageResponseObserver.value!!
    }

    var homepageResponseObserver = MutableLiveData<HomepageRepsonse>()
}