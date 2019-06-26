package com.audiospotapplication.UI.authors.Interface

import com.audiospotapplication.DataLayer.Model.AuthorsData

interface OnAuthorsItemClickListener {
    fun onAuthorItemClicked(authorsData: AuthorsData)
}