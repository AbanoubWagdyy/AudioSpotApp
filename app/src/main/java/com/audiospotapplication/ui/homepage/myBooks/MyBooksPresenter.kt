package com.audiospotapplication.ui.homepage.myBooks

import android.os.Handler
import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.model.BookListResponse
import com.audiospotapplication.data.retrofit.RetrofitCallbacks

import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class MyBooksPresenter(val mView: MyBooksContract.View) : MyBooksContract.Presenter {

    override fun saveBook(book: Book) {
        mRepositorySource.saveBook(book)
        mView.showBookDetailsScreen()
    }

    override fun start() {
        Handler().postDelayed({
            mView.showLoading()
            mRepositorySource = mView.getAppContext()?.let { DataRepository.getInstance(it) }!!
            mRepositorySource.getMyBooks(object : RetrofitCallbacks.BookListCallback {
                override fun onSuccess(result: BookListResponse?) {
                    if (result != null) {
                        mView.dismissLoading()
                        var listMyBooks = result?.data
                        if (listMyBooks != null && listMyBooks.isNotEmpty()) {
                            mView.setBookList(listMyBooks)
                        } else {
                            if (mRepositorySource.getAuthResponse() != null) {
                                mView.showEmptyBooksScreen("You have no books yet")
                            } else {
                                mView.showEmptyBooksScreen("You have to le be logged in")
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<BookListResponse>?, t: Throwable?) {
                    mView.dismissLoading()
                    mView.showErrorMessage()
                }
            })
        }, 100)
    }

    lateinit var mRepositorySource: RepositorySource
}