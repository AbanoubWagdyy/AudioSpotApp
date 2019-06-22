package com.audiospotapp.DataLayer.BusinessInterceptors.publishers

import com.audiospotapp.DataLayer.Model.AuthorsData
import com.audiospotapp.DataLayer.Model.PublishersResponseData

interface PublisherItemInterceptor {

    fun savePublisherItem(data: PublishersResponseData)

    fun getCurrentPublisherData(): PublishersResponseData?

    fun clearSavedPublisherData()
}