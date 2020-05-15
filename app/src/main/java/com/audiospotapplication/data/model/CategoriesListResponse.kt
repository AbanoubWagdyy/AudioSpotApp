package com.audiospot.DataLayer.Model

data class CategoriesListResponse(
    val APIVersion: String,
    val `data`: List<CategoriesListData>,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
)