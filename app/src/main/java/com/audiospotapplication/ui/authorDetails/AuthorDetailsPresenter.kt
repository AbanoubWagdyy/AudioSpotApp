package com.audiospotapplication.ui.authorDetails

import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.model.BookListResponse
import com.audiospotapplication.data.retrofit.RetrofitCallbacks
import com.audiospotapplication.data.retrofit.RetrofitResponseHandler
import com.audiospotapplication.data.RepositorySource
import retrofit2.Call

class AuthorDetailsPresenter(val mView: AuthorDetailsContract.View) : AuthorDetailsContract.Presenter {

    override fun saveBook(book: Book) {
        mRepositorySource.saveBook(book)
        mView.showBookDetailsScreen()
    }

    lateinit var mRepositorySource: RepositorySource

    override fun start() {
        mView.showLoading()
        mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
        var authorItem = mRepositorySource.getAuthorItem()
        if (authorItem != null) {
            mView.setAuthorName(authorItem.name)
        }
        if (authorItem != null) {
            mView.setAuthorImage(authorItem.photo)
        }
        if (authorItem != null) {
            mView.setAuthorBio(authorItem.bio)
        }
        mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
        if (authorItem != null) {
            mRepositorySource.getBooks(
                "", "", authorItem.id, 0, 0,
                0,
                object : RetrofitCallbacks.BookListCallback {
                    override fun onSuccess(result: BookListResponse?) {
                        mView.dismissLoading()
                        val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                        if (status == RetrofitResponseHandler.Companion.Status.VALID
                        ) {
                            mView.setBookList(result)
                        } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                            mView!!.showLoginPage()
                        } else {
                            mView!!.showErrorMessage(result!!.message)
                        }
                    }

                    override fun onFailure(call: Call<BookListResponse>?, t: Throwable?) {
                        mView.dismissLoading()
                        mView.showErrorMessage("Please check your internet connection")
                    }
                }
            )
        }
    }
}