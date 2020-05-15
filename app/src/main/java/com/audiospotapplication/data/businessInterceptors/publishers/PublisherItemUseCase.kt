package com.audiospotapplication.data.businessInterceptors.publishers

import com.audiospotapplication.data.model.PublishersResponseData

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