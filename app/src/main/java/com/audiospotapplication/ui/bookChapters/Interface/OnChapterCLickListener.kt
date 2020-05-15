package com.audiospotapplication.ui.bookChapters.Interface

import com.audiospotapplication.data.model.ChaptersData

interface OnChapterCLickListener {

    fun onChapterClicked(
        data: ChaptersData,
        position: Int
    )

    fun onItemDownloadPressed(data: ChaptersData)
}