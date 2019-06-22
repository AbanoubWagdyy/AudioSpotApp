package com.audiospotapp.DataLayer.BusinessInterceptors.authors

import com.audiospotapp.DataLayer.Model.AuthorsData

interface AuthorItemInterceptor {

    fun saveAuthorItem(data: AuthorsData)

    fun getCurrentAuthorData(): AuthorsData?

    fun clearSavedAuthorData()
}