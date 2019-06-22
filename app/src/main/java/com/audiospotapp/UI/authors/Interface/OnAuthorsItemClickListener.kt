package com.audiospotapp.UI.authors.Interface

import com.audiospotapp.DataLayer.Model.AuthorsData

interface OnAuthorsItemClickListener {
    fun onAuthorItemClicked(authorsData: AuthorsData)
}