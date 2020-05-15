package com.audiospotapplication.data.businessInterceptors.authors

import com.audiospotapplication.data.model.AuthorsData

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