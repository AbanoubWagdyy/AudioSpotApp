package com.audiospotapplication.ui.homepage

import androidx.lifecycle.MutableLiveData
import com.audiospot.DataLayer.Model.AuthResponse
import com.audiospotapplication.data.model.BaseViewModel
import com.audiospotapplication.data.model.BookListResponse
import com.audiospotapplication.data.retrofit.RetrofitCallbacks
import com.audiospotapplication.ui.ActiveTab
import retrofit2.Call

class HomepageViewModel : BaseViewModel() {

    init {
        getMyBooks()

        getMyCart()
    }

    fun getAuthResponse(): AuthResponse? {
        return mRepositorySource.getAuthResponse()
    }

    private fun getMyBooks() {
        if (getAuthResponse() != null)
            mRepositorySource.getMyBooks(object : RetrofitCallbacks.BookListCallback {
                override fun onSuccess(result: BookListResponse?) {
                }

                override fun onFailure(call: Call<BookListResponse>?, t: Throwable?) {
                }
            })
    }

    private fun getMyCart() {
        mRepositorySource.getMyCart(object : RetrofitCallbacks.BookListCallback {
            override fun onSuccess(result: BookListResponse?) {
                cartObserver.value = result?.data?.size
            }

            override fun onFailure(call: Call<BookListResponse>?, t: Throwable?) {
                cartObserver.value = 0
            }
        })
    }

    fun setActiveTab(activeTab: ActiveTab) {
        mRepositorySource.setActiveTab(activeTab)
    }

    fun getCartCountObserver(): MutableLiveData<Int> {
        return cartObserver
    }

    fun getCurrentLanguage(): String {
        return mRepositorySource.getCurrentLanguage()
    }

    var cartObserver = MutableLiveData<Int>()
}