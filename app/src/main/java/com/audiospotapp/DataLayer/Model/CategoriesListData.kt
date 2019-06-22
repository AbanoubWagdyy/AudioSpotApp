package com.audiospot.DataLayer.Model

data class CategoriesListData(
    val id: Int,
    val parent: Int,
    val parent_name: String,
    val title: String,
    val title_ar: String
)