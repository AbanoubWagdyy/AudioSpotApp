package com.audiospotapplication.UI.myBookmarks

import com.audiospot.DataLayer.Model.BookDetailsResponse
import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Model.Bookmark
import com.audiospotapplication.DataLayer.Model.MyBookmarksResponse
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.DataLayer.Retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class MyBookmarksPresenter(val mView: MyBookmarksContract.View) : MyBookmarksContract.Presenter {

    lateinit var mRepositorySource: RepositorySource

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
        mView.showLoading()
        mRepositorySource.myBookmarks(object : RetrofitCallbacks.MyBookmarkResponseCallback {
            override fun onSuccess(result: MyBookmarksResponse?) {
                mView.dismissLoading()
                val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                    mView.setBookmarks(result!!.data)
                } else {
                    mView.showMessage(result!!.message)
                }
            }

            override fun onFailure(call: Call<MyBookmarksResponse>?, t: Throwable?) {
                mView.dismissLoading()
                mView.showMessage("Please try again later")
            }
        })
    }

    override fun saveBookmark(bookmark: Bookmark) {
        mView.showLoading()
        mRepositorySource.getBookDetailsWithId(bookmark.book_id,
            object : RetrofitCallbacks.BookDetailsResponseCallback {

                override fun onFailure(call: Call<BookDetailsResponse>?, t: Throwable?) {
                    mView.dismissLoading()
                }

                override fun onSuccess(result: BookDetailsResponse?) {
                    mView.dismissLoading()
                    val status = RetrofitResponseHandler.validateAuthResponseStatus(result!!)
                    if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                        mRepositorySource.saveBook(result.data)
                        mRepositorySource.saveBookmark(bookmark)
                        mView.showBookChaptersScreen()
                    } else {
                        mView.showMessage("Please Check your internet connection")
                    }
                }
            })
    }
}