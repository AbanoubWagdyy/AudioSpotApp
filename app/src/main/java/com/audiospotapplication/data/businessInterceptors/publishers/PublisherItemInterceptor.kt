package com.audiospotapplication.data.businessInterceptors.publishers

import com.audiospotapplication.data.model.PublishersResponseData

interface PublisherItemInterceptor {

    fun savePublisherItem(data: PublishersResponseData)

    fun getCurrentPublisherData(): PublishersResponseData?

    fun clearSavedPublisherData()
}