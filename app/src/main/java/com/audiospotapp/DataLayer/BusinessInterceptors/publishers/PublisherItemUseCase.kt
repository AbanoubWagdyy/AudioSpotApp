package com.audiospotapp.DataLayer.BusinessInterceptors.publishers

import com.audiospotapp.DataLayer.Model.PublishersResponseData

class PublisherItemUseCase : PublisherItemInterceptor {

    override fun savePublisherItem(data: PublishersResponseData) {
        this.data = data
    }

    override fun getCurrentPublisherData(): PublishersResponseData? {
        return data
    }

    override fun clearSavedPublisherData() {
        data = null
    }

    var data: PublishersResponseData? = null
}