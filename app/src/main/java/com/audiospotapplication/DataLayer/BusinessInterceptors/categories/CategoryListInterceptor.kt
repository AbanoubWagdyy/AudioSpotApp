package com.audiospotapplication.DataLayer.BusinessInterceptors.categories

import com.audiospot.DataLayer.Model.CategoriesListData

interface CategoryListInterceptor {

    fun saveCategoryItem(categoryListData: CategoriesListData)

    fun getCurrentCategoryData(): CategoriesListData?

    fun clearSavedCategoryData()
}