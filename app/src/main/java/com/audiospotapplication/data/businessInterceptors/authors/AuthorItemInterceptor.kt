package com.audiospotapplication.data.businessInterceptors.authors

import com.audiospotapplication.data.model.AuthorsData

interface AuthorItemInterceptor {

    fun saveAuthorItem(data: AuthorsData)

    fun getCurrentAuthorData(): AuthorsData?

    fun clearSavedAuthorData()
}