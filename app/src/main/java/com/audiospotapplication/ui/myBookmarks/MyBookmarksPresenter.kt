package com.audiospotapplication.ui.myBookmarks

import com.audiospot.DataLayer.Model.BookDetailsResponse
import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.model.Bookmark
import com.audiospotapplication.data.model.ChaptersResponse
import com.audiospotapplication.data.model.MyBookmarksResponse
import com.audiospotapplication.data.retrofit.RetrofitCallbacks
import com.audiospotapplication.data.retrofit.RetrofitResponseHandler
import com.audiospotapplication.data.RepositorySource
import retrofit2.Call

class MyBookmarksPresenter(val mView: MyBookmarksContract.View) : MyBookmarksContract.Presenter {

    lateinit var mRepositorySource: RepositorySource

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
        mView.showLoading()
        mRepositorySource.myBookmarks(object : RetrofitCallbacks.MyBookmarkResponseCallback {
            override fun onSuccess(result: MyBookmarksResponse?) {
                mView.dismissLoading()
                val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                    mView.setBookmarks(result!!.data)
                } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                    mView!!.showLoginPage()
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
                    val status = RetrofitResponseHandler.validateAuthResponseStatus(result!!)
                    if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                        mRepositorySource.saveBook(result.data)
                        mRepositorySource.saveBookmark(bookmark)
                        mRepositorySource.getBookChapters(object :
                            RetrofitCallbacks.ChaptersResponseCallback {
                            override fun onSuccess(result: ChaptersResponse?) {
                                mView.dismissLoading()
                                if (status == RetrofitResponseHandler.Companion.Status.VALID && result != null) {
                                    mRepositorySource.setMediaItems(result.data)
                                    mView.showBookChaptersScreen()
                                }else{
                                    result?.message?.let { mView.showMessage(it) }
                                }
                            }

                            override fun onFailure(call: Call<ChaptersResponse>?, t: Throwable?) {
                                mView.dismissLoading()
                                mView.showMessage("Please Check your internet connection")
                            }
                        })

                    } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                        mView.dismissLoading()
                        mView!!.showLoginPage()
                    } else {
                        mView.dismissLoading()
                        mView.showMessage("Please Check your internet connection")
                    }
                }
            })
    }
}