package com.audiospotapp.UI.authors

import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Model.AuthorsData
import com.audiospotapp.DataLayer.Model.AuthorsResponse
import com.audiospotapp.DataLayer.Model.BookListResponse
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapp.DataLayer.Retrofit.RetrofitResponseHandler
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
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
        mRepositorySource.getAllAuthors(object : RetrofitCallbacks.AuthorsResponseCallback {
            override fun onSuccess(result: AuthorsResponse?) {
                mView.dismissLoading()
                val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                if (status == RetrofitResponseHandler.Status.VALID) {
                    mView.setAuthorsList(result)
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