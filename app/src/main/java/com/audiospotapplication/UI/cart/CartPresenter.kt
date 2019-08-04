package com.audiospotapplication.UI.cart

import com.audiospot.DataLayer.Model.AuthResponse
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Model.BookListResponse
import com.audiospotapplication.DataLayer.Model.Response
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.DataLayer.Retrofit.RetrofitResponseHandler

import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class CartPresenter(val mView: CartContract.View) : CartContract.Presenter {

    override fun handlePayNowPressed() {
        mView.showPaymentScreen()
    }

    override fun saveBook(book: Book) {
        mRepositorySource.saveBook(book)
        mView.showBookDetailsScreen()
    }

    override fun getAuthResponse(): AuthResponse? {
        return mRepositorySource.getAuthResponse()
    }

    override fun deleteBookFromCart(book: Book) {
        mView.showLoading()
        mRepositorySource.removeBookFromCart(book.id, object : RetrofitCallbacks.ResponseCallback {
            override fun onSuccess(result: Response?) {
                val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                    start()
                } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                    mView!!.showLoginPage()
                } else {
                    mView.dismissLoading()
                    mView.showMessage(result!!.message)
                }
            }

            override fun onFailure(call: Call<Response>?, t: Throwable?) {
                mView.dismissLoading()
                mView.showErrorMessage()
            }

            override fun onAuthFailure() {
                mView.dismissLoading()
            }
        })
    }

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
        mView.showLoading()
        mRepositorySource.getMyCart(object : RetrofitCallbacks.BookListCallback {
            override fun onSuccess(result: BookListResponse?) {
                mView.dismissLoading()
                val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                    mView.setBookList(result!!.data)
                    mView.setCartCount(result.data.size)
                } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                    mView!!.showLoginPage()
                } else {
                    result?.message?.let { mView.showMessage(it) }
                }
            }

            override fun onFailure(call: Call<BookListResponse>?, t: Throwable?) {
                mView.dismissLoading()
                mView.showErrorMessage()
            }
        })
    }

    lateinit var mRepositorySource: RepositorySource
}