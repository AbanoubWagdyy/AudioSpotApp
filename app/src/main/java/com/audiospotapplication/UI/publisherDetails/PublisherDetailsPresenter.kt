package com.audiospotapplication.UI.publisherDetails

import com.audiospot.DataLayer.Model.Book
import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Model.BookListResponse
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.DataLayer.Retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class PublisherDetailsPresenter(val mView: PublisherDetailsContract.View) : PublisherDetailsContract.Presenter {

    override fun saveBook(book: Book) {
        mRepositorySource.saveBook(book)
        mView.showBookDetailsScreen()
    }

    lateinit var mRepositorySource: RepositorySource

    override fun start() {
        mView.showLoading()
        mRepositorySource = mView.getAppContext()?.let { DataRepository.getInstance(it) }!!
        var publisherItem = mRepositorySource.getPublisherItem()
        if (publisherItem != null) {
            mView.setPublisherName(publisherItem.name)
        }
        if (publisherItem != null) {
            mView.setPublisherImage(publisherItem.photo)
        }
        if (publisherItem != null) {
            mView.setPublisherBio(publisherItem.bio)
        }
        if (publisherItem != null) {
            mRepositorySource.getBooks(
                "", "", 0, publisherItem.id, 0,
                0,
                object : RetrofitCallbacks.BookListCallback {
                    override fun onSuccess(result: BookListResponse?) {
                        mView.dismissLoading()
                        val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                        if (status == RetrofitResponseHandler.Companion.Status.VALID) {
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