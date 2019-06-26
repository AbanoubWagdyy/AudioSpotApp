package com.audiospotapplication.UI.categories

import android.content.Context
import com.audiospot.DataLayer.Model.CategoriesListData
import com.audiospot.DataLayer.Model.CategoriesListResponse

interface CategoriesContract {

    interface Presenter {
        fun start()
        fun handleCategoryItemClicked(categoryListData: CategoriesListData)
    }

    interface View {
        fun getAppContext(): Context?
        fun setCategoriesList(result: CategoriesListResponse?)
        fun showErrorMessage(message: String)
        fun showBooksScreen()
        fun showLoadingDialog()
        fun dismissLoadingDialog()
    }
}