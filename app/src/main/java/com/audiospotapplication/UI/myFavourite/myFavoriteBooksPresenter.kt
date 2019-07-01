package com.audiospotapplication.UI.myFavourite

import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Model.BookListResponse
import com.audiospotapplication.DataLayer.Model.Response
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.DataLayer.Retrofit.RetrofitResponseHandler

import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class myFavoriteBooksPresenter(val mView: myFavouriteBooksContract.View) : myFavouriteBooksContract.Presenter {

    override fun removeFromFavorites(book: Book) {
        mView.showLoading()
        mRepositorySource.removeBookFromFavorites(book.id,
            object : RetrofitCallbacks.ResponseCallback {

                override fun onAuthFailure() {
                    mView.dismissLoading()
                }

                override fun onSuccess(result: Response?) {
                    mView.dismissLoading()
                    val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                    mView.showMessage(result!!.message)
                    if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                        mRepositorySource.getMyFavouriteBooks(object : RetrofitCallbacks.BookListCallback {
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
                }

                override fun onFailure(call: Call<Response>?, t: Throwable?) {
                    mView.dismissLoading()
                    mView.showErrorMessage()
                }
            })
    }

    override fun saveBook(book: Book) {
        mRepositorySource.saveBook(book)
        mView.showBookDetailsScreen()
    }

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
        mView.showLoading()
        mRepositorySource.getMyFavouriteBooks(object : RetrofitCallbacks.BookListCallback {
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