package com.audiospotapplication.UI.categories

import android.content.Context
import com.audiospot.DataLayer.Model.CategoriesListData
import com.audiospot.DataLayer.Model.CategoriesListResponse
import com.audiospotapplication.UI.BaseView

interface CategoriesContract {

    interface Presenter {
        fun start()
        fun handleCategoryItemClicked(categoryListData: CategoriesListData)
    }

    interface View : BaseView {
        fun getAppContext(): Context?
        fun setCategoriesList(result: CategoriesListResponse?)
        fun showErrorMessage(message: String)
        fun showBooksScreen()
        fun showLoadingDialog()
        fun dismissLoadingDialog()
    }
}