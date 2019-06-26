package com.audiospotapplication.DataLayer.BusinessInterceptors.authors

import com.audiospotapplication.DataLayer.Model.AuthorsData

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