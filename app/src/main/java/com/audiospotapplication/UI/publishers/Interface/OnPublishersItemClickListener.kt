package com.audiospotapplication.UI.publishers.Interface

import com.audiospotapplication.DataLayer.Model.PublishersResponseData

interface OnPublishersItemClickListener {
    fun onPublisherItemClicked(data: PublishersResponseData)
}