package com.audiospotapplication.data.model

import com.audiospot.DataLayer.Model.AuthData

data class LogoutAuthResponse(
    val APIVersion: String,
    val `data`: List<AuthData>,
    val isLogin: Int,
    val lang: String,
    val message: String,
    val status: Int
)