package com.audiospotapplication.UI.bookChapters.Interface

import com.audiospotapplication.DataLayer.Model.ChaptersData

interface OnChapterCLickListener {

    fun onChapterClicked(
        data: ChaptersData,
        position: Int
    )
}