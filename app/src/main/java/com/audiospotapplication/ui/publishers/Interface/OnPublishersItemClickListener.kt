package com.audiospotapplication.ui.publishers.Interface

import com.audiospotapplication.data.model.PublishersResponseData

interface OnPublishersItemClickListener {
    fun onPublisherItemClicked(data: PublishersResponseData)
}