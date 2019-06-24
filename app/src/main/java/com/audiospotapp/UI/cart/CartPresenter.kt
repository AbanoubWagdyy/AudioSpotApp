package com.audiospotapp.UI.cart

import com.audiospot.DataLayer.Model.AuthResponse
import com.audiospot.DataLayer.Model.Book
import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Model.BookListResponse
import com.audiospotapp.DataLayer.Model.Response
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapp.DataLayer.Retrofit.RetrofitResponseHandler

import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class CartPresenter(val mView: CartContract.View) : CartContract.Presenter {

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
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
        mView.showLoading()
        mRepositorySource.getMyCart(object : RetrofitCallbacks.BookListCallback {
            override fun onSuccess(result: BookListResponse?) {
                mView.dismissLoading()
                mView.setBookList(result!!.data)
            }

            override fun onFailure(call: Call<BookListResponse>?, t: Throwable?) {
                mView.dismissLoading()
                mView.showErrorMessage()
            }
        })
    }

    lateinit var mRepositorySource: RepositorySource
}