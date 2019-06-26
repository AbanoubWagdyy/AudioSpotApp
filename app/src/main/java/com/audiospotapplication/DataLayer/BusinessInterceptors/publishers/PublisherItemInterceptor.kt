package com.audiospotapplication.DataLayer.BusinessInterceptors.publishers

import com.audiospotapplication.DataLayer.Model.PublishersResponseData

interface PublisherItemInterceptor {

    fun savePublisherItem(data: PublishersResponseData)

    fun getCurrentPublisherData(): PublishersResponseData?

    fun clearSavedPublisherData()
}