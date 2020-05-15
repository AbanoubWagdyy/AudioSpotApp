package com.audiospotapplication.UI.homepage

import androidx.lifecycle.MutableLiveData
import com.audiospot.DataLayer.Model.AuthResponse
import com.audiospotapplication.DataLayer.Model.BaseViewModel
import com.audiospotapplication.DataLayer.Model.BookListResponse
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.UI.ActiveTab
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

    var cartObserver = MutableLiveData<Int>()
}