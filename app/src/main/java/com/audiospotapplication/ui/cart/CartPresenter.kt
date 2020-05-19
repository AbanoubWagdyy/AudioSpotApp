package com.audiospotapplication.ui.cart

import com.audiospot.DataLayer.Model.AuthResponse
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.model.BookListResponse
import com.audiospotapplication.data.model.Response
import com.audiospotapplication.data.retrofit.RetrofitCallbacks
import com.audiospotapplication.data.retrofit.RetrofitResponseHandler

import com.audiospotapplication.data.RepositorySource
import retrofit2.Call

class CartPresenter(val mView: CartContract.View) : CartContract.Presenter {

    override fun handlePayNowPressed() {
        mView.showPaymentScreen()
    }

    override fun getBooksPrice(): Int? {
        return priceTotal
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
                when (RetrofitResponseHandler.validateAuthResponseStatus(result)) {
                    RetrofitResponseHandler.Companion.Status.VALID -> {
                        start()
                    }
                    RetrofitResponseHandler.Companion.Status.UNAUTHORIZED -> {
                        mView.showLoginPage()
                    }
                    else -> {
                        mView.dismissLoading()
                        mView.showMessage(result!!.message)
                    }
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
                when (RetrofitResponseHandler.validateAuthResponseStatus(result)) {
                    RetrofitResponseHandler.Companion.Status.VALID -> {
                        mView.setBookList(result!!.data)
                        for (book in result!!.data) {
                            priceTotal += book.price
                        }
                        mView.setCartCount(result.data.size)
                    }
                    RetrofitResponseHandler.Companion.Status.UNAUTHORIZED -> {
                        mView.showLoginPage()
                    }
                    else -> {
                        result?.message?.let { mView.showMessage(it) }
                    }
                }
            }

            override fun onFailure(call: Call<BookListResponse>?, t: Throwable?) {
                mView.dismissLoading()
                mView.showErrorMessage()
            }
        })
    }

    lateinit var mRepositorySource: RepositorySource
    var priceTotal = 0
}