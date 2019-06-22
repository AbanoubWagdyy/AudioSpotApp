package com.audiospotapp.DataLayer.BusinessInterceptors.categories

import com.audiospot.DataLayer.Model.CategoriesListData

class CategoryListUseCase : CategoryListInterceptor {

    override fun clearSavedCategoryData() {
        mCategoryListData = null
    }

    var mCategoryListData: CategoriesListData? = null

    override fun saveCategoryItem(categoriesListData: CategoriesListData) {
        mCategoryListData = categoriesListData
    }

    override fun getCurrentCategoryData(): CategoriesListData? = mCategoryListData
}