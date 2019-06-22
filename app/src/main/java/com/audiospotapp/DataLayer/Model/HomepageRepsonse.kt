package com.audiospot.DataLayer.Model

data class HomepageRepsonse(
    val APIVersion: String,
    val `data`: List<HomepageData>,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
)