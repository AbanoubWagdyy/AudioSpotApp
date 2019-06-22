package com.audiospotapp.DataLayer.BusinessInterceptors.authors

import com.audiospotapp.DataLayer.Model.AuthorsData

class AuthorItemUseCase : AuthorItemInterceptor {

    var data: AuthorsData? = null

    override fun saveAuthorItem(data: AuthorsData) {
        this.data = data
    }

    override fun getCurrentAuthorData(): AuthorsData? {
        return data
    }

    override fun clearSavedAuthorData() {
        data = null
    }
}