package com.audiospotapplication.UI.addBookmark

import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Model.BookmarkBody
import com.audiospotapplication.DataLayer.Model.Response
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.DataLayer.Retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class AddBookmarkPresenter(val mView: AddBookmarkContract.View) : AddBookmarkContract.Presenter {

    override fun addBookmark(toString: String) {
        mView.showLoading()
        bookmarkData.comment = toString
        mRepositorySource.addBookmark(bookmarkData, object : RetrofitCallbacks.ResponseCallback {
            override fun onSuccess(response: Response?) {
                mView.dismissLoading()
                var result = RetrofitResponseHandler.validateAuthResponseStatus(response)
                mView.showMessage(response!!.message)
                if (result == RetrofitResponseHandler.Companion.Status.VALID) {
                    mView.finalizeView()
                }
            }

            override fun onFailure(call: Call<Response>?, t: Throwable?) {
                mView.dismissLoading()
                mView.showMessage("Please try again later")
            }

            override fun onAuthFailure() {
                mView.dismissLoading()
                mView.showMessage("Please try again later")
            }
        })
    }

    private lateinit var bookmarkData: BookmarkBody

    lateinit var mRepositorySource: RepositorySource

    override fun start() {
        mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
        bookmarkData = mRepositorySource.getBookmarkData()!!
        mView.setBookName(bookmarkData.bookName)
        mView.setBookImage(bookmarkData.url)
        mView.setChapterName(bookmarkData.chapter_name)
        mView.setBookmarkTime(bookmarkData.bookmarkTime)
    }
}