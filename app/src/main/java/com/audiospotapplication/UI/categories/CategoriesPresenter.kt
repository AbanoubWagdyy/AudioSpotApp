package com.audiospotapplication.UI.categories

import com.audiospot.DataLayer.Model.CategoriesListData
import com.audiospot.DataLayer.Model.CategoriesListResponse
import com.audiospotapplication.DataLayer.DataRepository
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.DataLayer.Retrofit.RetrofitResponseHandler
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call

class CategoriesPresenter(val mView: CategoriesContract.View) : CategoriesContract.Presenter {

    override fun handleCategoryItemClicked(categoryListData: CategoriesListData) {
        mRepositorySource.saveCategoryItem(categoryListData)
        mView.showBooksScreen()
    }

    lateinit var mRepositorySource: RepositorySource

    override fun start() {
        mView.showLoadingDialog()
        mRepositorySource = DataRepository.getInstance(mView.getAppContext())
        mRepositorySource.getAllCategories(object : RetrofitCallbacks.CategoriesListCallback {
            override fun onSuccess(result: CategoriesListResponse?) {
                mView.dismissLoadingDialog()
                val status = RetrofitResponseHandler.validateAuthResponseStatus(result)
                if (status == RetrofitResponseHandler.Companion.Status.VALID) {
                    mView.setCategoriesList(result)
                } else {
                    mView!!.showErrorMessage(result!!.message)
                }
            }

            override fun onFailure(call: Call<CategoriesListResponse>?, t: Throwable?) {
                mView.dismissLoadingDialog()
                mView.showErrorMessage("Can not get AuthorsData ,please try again later")
            }
        })
    }
}