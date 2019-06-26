package com.audiospotapplication.DataLayer.BusinessInterceptors.authors

import com.audiospotapplication.DataLayer.Model.AuthorsData

interface AuthorItemInterceptor {

    fun saveAuthorItem(data: AuthorsData)

    fun getCurrentAuthorData(): AuthorsData?

    fun clearSavedAuthorData()
}