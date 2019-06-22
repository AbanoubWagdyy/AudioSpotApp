package com.audiospotapp.UI.bookChapters

import com.audiospotapp.DataLayer.DataRepository
import com.audiospotapp.DataLayer.Model.ChaptersResponse
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapp.DataLayer.Retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class BookChaptersPresenter(val mView: BookChaptersContract.View) : BookChaptersContract.Presenter {

    override fun start() {
        mRepoSource = DataRepository.getInstance(mView.getAppContext())
        mView.showLoadingDialog()
        mRepoSource.getBookChapters(object : RetrofitCallbacks.ChaptersResponseCallback {
            override fun onSuccess(result: ChaptersResponse?) {
                mView.dismissLoading()
                val status = RetrofitResponseHandler.validateResponseStatus(result)
                if (status == RetrofitResponseHandler.Status.VALID) {
                    mView.setChapters(result!!.data)
                }
            }

            override fun onFailure(call: Call<ChaptersResponse>?, t: Throwable?) {
                mView.dismissLoading()
            }
        })
    }

    lateinit var mRepoSource: RepositorySource
}