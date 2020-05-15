package com.audiospotapplication.ui.authors.Interface

import com.audiospotapplication.data.model.AuthorsData

interface OnAuthorsItemClickListener {
    fun onAuthorItemClicked(authorsData: AuthorsData)
}