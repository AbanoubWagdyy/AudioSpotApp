package com.audiospotapplication.UI.homepage.home

import com.audiospot.DataLayer.Model.Book
import com.audiospot.DataLayer.Model.HomepageRepsonse
import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.DataLayer.Retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class HomePresenter(val mView: HomeContract.View) :
    HomeContract.Presenter {

    override fun saveBook(book: Book) {
        mRepoSource.saveBook(book)
        mView.showBookDetailsScreen()
    }

    override fun start() {
        mView.showDialog()
        mRepoSource = DataRepository.getInstance(mView.getContainingActivity().applicationContext)
        mRepoSource.getHomepage(object : RetrofitCallbacks.HomepageResponseCallback {
            override fun onSuccess(result: HomepageRepsonse?) {
                mView.hideDialog()
                val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                    mView.setHomeResponse(result)
                } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                    mView!!.showLoginPage()
                } else {
                    mView!!.showErrorMessage(result!!.message)
                }
            }

            override fun onFailure(call: Call<HomepageRepsonse>?, t: Throwable?) {
                mView.hideDialog()
                mView!!.showErrorMessage("Server Error,Please try again !.")
            }
        })
    }

    lateinit var mRepoSource: RepositorySource
}