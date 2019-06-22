package com.audiospotapp.UI.publishers.Interface

import com.audiospotapp.DataLayer.Model.AuthorsData
import com.audiospotapp.DataLayer.Model.PublishersResponseData

interface OnPublishersItemClickListener {
    fun onPublisherItemClicked(data: PublishersResponseData)
}