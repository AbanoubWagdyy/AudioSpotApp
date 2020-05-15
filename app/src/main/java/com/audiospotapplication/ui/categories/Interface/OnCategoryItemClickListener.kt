package com.audiospotapplication.ui.categories.Interface

import com.audiospot.DataLayer.Model.CategoriesListData

interface OnCategoryItemClickListener {

    fun onCategoryItemClicked(categoriesListData: CategoriesListData)
}