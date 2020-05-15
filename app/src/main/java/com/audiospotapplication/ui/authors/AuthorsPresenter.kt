package com.audiospotapplication.ui.authors

import com.audiospotapplication.data.DataRepository
import com.audiospotapplication.data.model.AuthorsData
import com.audiospotapplication.data.model.AuthorsResponse
import com.audiospotapplication.data.retrofit.RetrofitCallbacks
import com.audiospotapplication.data.retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class AuthorsPresenter(val mView: AuthorsContract.View) : AuthorsContract.Presenter {
    override fun handleAuthorItemClicked(authorsData: AuthorsData) {
        mRepositorySource.saveAuthorItem(authorsData)
        mView.showAuthorDetailsScreen()
    }

    lateinit var mRepositorySource: RepositorySource

    override fun start() {
        mView.showLoading()
        mRepositorySource = DataRepository.getInstance(mView.getAppContext()!!)
        mRepositorySource.getAllAuthors(object : RetrofitCallbacks.AuthorsResponseCallback {
            override fun onSuccess(result: AuthorsResponse?) {
                mView.dismissLoading()
                val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                    mView.setAuthorsList(result)
                } else if (status == RetrofitResponseHandler.Companion.Status.UNAUTHORIZED) {
                    mView!!.showLoginPage()
                } else {
                    mView!!.showErrorMessage(result!!.message)
                }
            }

            override fun onFailure(call: Call<AuthorsResponse>?, t: Throwable?) {
                mView.dismissLoading()
                mView!!.showErrorMessage("Server Error ,please try again later")
            }
        })
    }
}